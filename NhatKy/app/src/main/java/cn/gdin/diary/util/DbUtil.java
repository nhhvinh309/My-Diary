package cn.gdin.diary.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.gdin.diary.domain.DiaryItem;

public class DbUtil {

	private SQLiteDatabase db;
	private DbHelper dbHelper;

	public DbUtil(Context context) {
		dbHelper = new DbHelper(context);
	}

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}


	public void insert(String title, String content) {

		this.open();
		/* ContentValues */
		ContentValues cv = new ContentValues();

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(d);

		String week = getWeekOfDate(d);

		cv.put(DbHelper.DIARY_DATE, date);
		cv.put(DbHelper.DIARY_WEEK, week);
		cv.put(DbHelper.DIARY_TITLE, title);
		cv.put(DbHelper.DIARY_CONTENT, content);

		db.insert(DbHelper.TABLE_NAME, null, cv);
		this.close();
	}


	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "Chủ nhật", "Thứ hai", "Thứ ba", "Thứ tư", "Thứ năm", "Thứ sáu", "Thứ bảy" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}


	public boolean delete(String id) {
		this.open();
		String where = DbHelper.DIARY_ID + " = ?";
		String[] whereValue = { id };
		if (db.delete(DbHelper.TABLE_NAME, where, whereValue) > 0) {
			this.close();
			return true;
		}
		return false;

	}


	public boolean update(String id, String title, String content) {
		this.open();
		ContentValues cv = new ContentValues();
		cv.put(DbHelper.DIARY_TITLE, title);
		cv.put(DbHelper.DIARY_CONTENT, content);
		if (db.update(DbHelper.TABLE_NAME, cv, DbHelper.DIARY_ID + "=?",
				new String[] { id }) > 0) {
			this.close();
			return true;
		}
		db.close();
		return false;
	}

	public ArrayList<DiaryItem> getAllData() {
		this.open();
		ArrayList<DiaryItem> items = new ArrayList<DiaryItem>();
		Cursor cursor = db.rawQuery("select * from diary", null);

		if (cursor.moveToFirst()) {

			while (!cursor.isAfterLast()) {
				String id = cursor.getString(cursor
						.getColumnIndex(DbHelper.DIARY_ID));
				String date = cursor.getString(cursor
						.getColumnIndex(DbHelper.DIARY_DATE));
				String week = cursor.getString(cursor
						.getColumnIndex(DbHelper.DIARY_WEEK));
				String title = cursor.getString(cursor
						.getColumnIndex(DbHelper.DIARY_TITLE));
				String content = cursor.getString(cursor
						.getColumnIndex(DbHelper.DIARY_CONTENT));

				DiaryItem item = new DiaryItem(id, date, week, title, content);
				items.add(item);
				cursor.moveToNext();
			}
		}
		this.close();
		return items;
	}
}
