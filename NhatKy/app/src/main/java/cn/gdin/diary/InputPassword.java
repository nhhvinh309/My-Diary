package cn.gdin.diary;

import cn.gdin.diary.util.MD5Util;
import cn.gdin.diary.util.MyApplication;
import cn.gdin.diary.util.Util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputPassword extends Activity {
	private Button mBtnPwdOk;
	private Button mBtnCancel;
	private EditText mPassword;
	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pwd);
		
		MyApplication.getInstance().addActivity(this);
		
		mPassword = (EditText) findViewById(R.id.et_pwd); // Điền mật khẩu hộp văn bản
		mBtnPwdOk = (Button) findViewById(R.id.btn_pwd_ok);// Xác nhận mật khẩu
		mBtnCancel = (Button) findViewById(R.id.btn_pwd_cancel);// Hủy bỏ
		// Giai mã
		pwd = MD5Util.JM(MD5Util.KL(Util.loadData(this)));
		
		// Xác định xem mật khẩu có rỗng k
		if (!TextUtils.isEmpty(pwd)) {
			pwdBtnOnclick();
		} else {
			//Chuyển đến ItemActivity, xử lý liên quan đến hoạt động (thiết lập mật khẩu)
			Intent intent = new Intent();
			intent.setClass(InputPassword.this, ItemActivity.class);
			startActivity(intent);
			finish();
		}

	}

	// nhập mật khẩu
	private void pwdBtnOnclick() {

		mBtnPwdOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = mPassword.getText().toString();
				//Sau MD5 mã hóa mật khẩu vào để phù hợp với mật khẩu đã nhập
				String passwd = MD5Util.MD5(password);
				
				if (passwd.equals(pwd)) {
					Intent intent = new Intent();
					intent.setClass(InputPassword.this, ItemActivity.class);
					startActivity(intent);
					finish();
				} else {
					// Nhắc nhỏ khi sai mật khẩu
					Toast.makeText(InputPassword.this, "Sai mật khẩu", 0).show();
				}

			}
		});
		// Nhấp cancel để thoát khỏi UD
		mBtnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
