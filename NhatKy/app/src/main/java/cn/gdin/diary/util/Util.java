package cn.gdin.diary.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.gdin.diary.R;


public class Util {

	private static AlertDialog mAlertDialog;

	public static final String FILE_NAME_SAVE_DATA = "data.dat";

	public static void savaData(Context context, String password) {
		FileOutputStream fos = null;

		try {
			fos = context.openFileOutput(FILE_NAME_SAVE_DATA,
					Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeBytes(password);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	public static String loadData(Context context) {
		FileInputStream fis = null;

		String password = "";
		try {
			fis = context.openFileInput(FILE_NAME_SAVE_DATA);

			DataInputStream dis = new DataInputStream(fis);
			byte[] b = new byte[1024];
			int len = dis.read(b);

			password = new String(b, 0, len);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return password;
	}
	public static View getView(Context context, int layoutId) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(layoutId, null);
		return layout;
	}
	@SuppressLint("NewApi")
	public static void showDialog(final Context context, String message,
			final IAlertDialogButtonListener listener) {
		View dialogView = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				R.style.Theme_Transparent);
		dialogView = getView(context, R.layout.dialog_view);

		Button btnOkView = (Button) dialogView.findViewById(R.id.btn_dialog_ok);
		Button btnCancelView = (Button) dialogView
				.findViewById(R.id.btn_dialog_cancel);
		TextView txtMessageView = (TextView) dialogView
				.findViewById(R.id.text_dialog_message);

		txtMessageView.setText(message);


		btnOkView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mAlertDialog != null) {
					mAlertDialog.cancel();
				}

				if (listener != null) {
					listener.onClick();
				}

			}
		});


		btnCancelView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mAlertDialog != null) {
					mAlertDialog.cancel();
				}

			}
		});


		builder.setView(dialogView);

		mAlertDialog = builder.create();
		mAlertDialog.show();
	}

}
