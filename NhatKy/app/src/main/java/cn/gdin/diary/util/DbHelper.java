package cn.gdin.diary.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {

	public final static String DATABASE_NAME = "MyData.db";
	public static int DATABASE_VERSION = 1;
	public final static String TABLE_NAME = "diary";
	public final static String DIARY_ID = "id";
	public final static String DIARY_DATE = "date";
	public final static String DIARY_WEEK = "week";
	public final static String DIARY_TITLE = "title";
	public final static String DIARY_CONTENT = "content";

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
				+ DIARY_ID + " INTEGER primary key autoincrement, "
				+ DIARY_DATE + " text, " + DIARY_WEEK + " text," + DIARY_TITLE
				+ " text," + DIARY_CONTENT + " text);";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}
}
