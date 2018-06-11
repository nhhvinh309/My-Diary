package cn.gdin.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cn.gdin.diary.domain.DiaryItem;
import cn.gdin.diary.util.DbUtil;
import cn.gdin.diary.util.IAlertDialogButtonListener;
import cn.gdin.diary.util.MD5Util;
import cn.gdin.diary.util.MyApplication;
import cn.gdin.diary.util.Util;

public class ItemActivity extends Activity {

	private LinearLayout mLayoutSetting;

	private RelativeLayout mLayoutList;

	private ListView mListView;

	private Button mBtnSetPwd;
	private Button mBtnAdd;

	private EditText mSetPassword;
	private EditText mRepPassword;

	ArrayList<DiaryItem> items;

	MyAdapter myAdapter;
	DbUtil util;

	private String flag; // Người dùng nhấp Cancel để thoát

	public final static String SER_KEY = "cn.gdin.diary.ser";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_all);

		MyApplication.getInstance().addActivity(this);

		initView();
		loadView();

		initData();

		// thêm nhật ký
		addBtnOnClick();
	}

	// Nội dung hiển thị listview
	private void initData() {
		util = new DbUtil(this);

		items = util.getAllData();

		myAdapter = new MyAdapter(items, this);

		mListView.setAdapter(myAdapter);
		// Nhấn vào đây để xem chi tiết
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long line) {
				String id = items.get(position).getId();
				String date = items.get(position).getDate();
				String week = items.get(position).getWeek();
				String title = items.get(position).getTitle();
				String content = items.get(position).getContent();
				// Log.i("my", id+":"+title+":"+content);
				flag = "1";

				DiaryItem mDiary = new DiaryItem();
				mDiary.setId(id);
				mDiary.setDate(date);
				mDiary.setWeek(week);
				mDiary.setTitle(title);
				mDiary.setContent(content);

				Intent intent = new Intent(ItemActivity.this,
						MainActivity.class);
				Bundle mBundle = new Bundle();
				mBundle.putSerializable(SER_KEY, mDiary);
				intent.putExtras(mBundle);
				intent.putExtra("flag", flag);
				startActivity(intent);
			}
		});
		// nhấp vào để xóa nhật ký và hiện thông báo
		mListView
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu arg0,
							View arg1, ContextMenuInfo arg2) {
						arg0.setHeaderTitle("Xóa");
						arg0.add(0, 0, 0, "Đồng ý");
						arg0.add(0, 1, 0, "Hủy bỏ");

					}
				});

	}

	// Menu chức năng
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// item.getItemId()
		if (item.getItemId() == 0) {

			int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
			String id = items.get(selectedPosition).getId();
			// xóa nhật ký
			util.delete(id);
			items.remove(items.get(selectedPosition));
			myAdapter.notifyDataSetChanged();

		}

		return super.onContextItemSelected(item);
	}

	// Khởi tạo để chỉnh sửa mật khẩu.
	private void initView() {
		// Giao diện mật khẩu
		mLayoutSetting = (LinearLayout) findViewById(R.id.layout_setting);
		mSetPassword = (EditText) findViewById(R.id.et_set_pwd);
		mBtnSetPwd = (Button) findViewById(R.id.btn_set_pwd_ok);
		mRepPassword = (EditText) findViewById(R.id.et_rep_pwd);

		// Giao diện nhật ký của tôi
		mLayoutList = (RelativeLayout) findViewById(R.id.layout_list);
		mListView = (ListView) findViewById(R.id.listView);
		mBtnAdd = (Button) findViewById(R.id.add_diary);

	}

	// Mật khẩu không tồn tại, màn hình cài đặt mật khẩu
	private void loadView() {
		if (TextUtils.isEmpty(Util.loadData(this))) {
			mLayoutSetting.setVisibility(View.VISIBLE);
			SetBtnOnclick();
		}
	}

	// mật khẩu
	private void SetBtnOnclick() {
		mBtnSetPwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String passwd = mSetPassword.getText().toString();
				String rePassword = mRepPassword.getText().toString();
				if (!passwd.equals(rePassword)) {
					Toast.makeText(ItemActivity.this, "Mật khẩu không trùng khớp",
							Toast.LENGTH_SHORT).show();
				} else {
					String setPasswd = MD5Util.MD5(passwd);
					Util.savaData(ItemActivity.this, setPasswd);
					mLayoutList.setVisibility(View.VISIBLE);
					mLayoutSetting.setVisibility(View.GONE);
				}
			}
		});
	}

	// Chuyển đến thêm một cuốn nhật ký mới
	private void addBtnOnClick() {
		mBtnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = "0";
				Intent intent = new Intent();
				intent.setClass(ItemActivity.this, MainActivity.class);
				intent.putExtra("flag", flag);
				startActivity(intent);
				finish();
			}
		});
	}

	// Quay trở lại các tính năng tạo mật khẩu
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showConfirmDialog();
			return true;
		}
		return false;

	}

	// kết thúc tiến trình
	private IAlertDialogButtonListener mBtnOkQuitClickListener = new IAlertDialogButtonListener() {

		@Override
		public void onClick() {
			// đống tất cả các Activity
			MyApplication.getInstance().exit();
			int id = android.os.Process.myPid();
			if (id != 0) {
				android.os.Process.killProcess(id);
			}
		}

	};

	// nhắc nhở
	private void showConfirmDialog() {
		Util.showDialog(this, "Chắc chắn thoát chứ !", mBtnOkQuitClickListener);
	}

}
