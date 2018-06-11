package cn.gdin.diary;

import java.text.SimpleDateFormat;

import java.util.Date;

import cn.gdin.diary.domain.DiaryItem;
import cn.gdin.diary.util.DbUtil;
import cn.gdin.diary.util.MyApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button mBackBtn;
	private Button mSaveBtn;

	private EditText mTextTitle;
	private EditText mTextContent;

	private TextView mTextDate;
	private TextView mTextWeek;
	private TextView mTVTitle;

	private String flag;
	private String mId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		MyApplication.getInstance().addActivity(this);

		initView();

		Intent intent = getIntent();
		flag = intent.getStringExtra("flag");
		if (flag.equals("0")) {
			mTVTitle.setText("Nhật ký");
			initDate();
		} else {
			initUpdateData();
		}
		backOnClick();
		saveOnClick();
	}
	public void initView() {
		mBackBtn = (Button) findViewById(R.id.btn_back);
		mSaveBtn = (Button) findViewById(R.id.btn_save);
		mTextTitle = (EditText) findViewById(R.id.et_title);
		mTextContent = (EditText) findViewById(R.id.et_content);
		mTextDate = (TextView) findViewById(R.id.tv_date);
		mTextWeek = (TextView) findViewById(R.id.tv_week);

		mTVTitle = (TextView) findViewById(R.id.tv_top_title);
	}

	// cập nhập ngày hôm nay
	private void initDate() {

		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(d);

		String week = DbUtil.getWeekOfDate(d);
		mTextDate.setText(date);
		mTextWeek.setText(week);
	}

	// cập nhật dữ liệu
	private void initUpdateData() {
		DiaryItem mDiary = (DiaryItem) getIntent().getSerializableExtra(
				ItemActivity.SER_KEY);
		mId = mDiary.getId();
		mTextDate.setText(mDiary.getDate());
		mTextWeek.setText(mDiary.getWeek());
		mTextTitle.setText(mDiary.getTitle());
		mTextContent.setText(mDiary.getContent());

		mTVTitle.setText("Sửa nhật ký");
	}

	// Quay lại danh sách nhật ký của tôi
	private void backOnClick() {
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ItemActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

		// Lưu nhật ký
	private void saveOnClick() {
		mSaveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DbUtil db = new DbUtil(MainActivity.this);

				String title = mTextTitle.getText().toString();
				String content = mTextContent.getText().toString();
				if (title.equals("") || content.equals("")) {
					Toast.makeText(MainActivity.this, "Tiêu đề và nội dung không thể để trống !", 0).show();
				} else {
					if (flag.equals("0")) {
						db.insert(title, content);
						Toast.makeText(MainActivity.this, "Đã lưu thành công !", 0).show();

					} else {
						db.update(mId, title, content);
						Toast.makeText(MainActivity.this, "Cập nhật thành công !", 0).show();

					}

				}

			}
		});
	}
	// Quay trở lại các tính năng tạo mật khẩu
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ItemActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return false;

	}

}
