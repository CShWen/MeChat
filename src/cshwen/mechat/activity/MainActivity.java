package cshwen.mechat.activity;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.Constants;
import cshwen.mechat.utils.Tool;
import cshwen.mechat.utils.XmlUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	ProgressDialog progress;
	ImManager im;
	XmlUtil uxml;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.LOGINING:
				showProgressDialog("CShWen登录中");
				break;
			case Constants.LOGINED:
				dismissProgressDialog();
				Toast.makeText(getApplicationContext(), "CShWen登录成功",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this,
						HomeActivity.class);
				startActivity(intent);
				break;
			case Constants.LOGIN_FAILURE:
				dismissProgressDialog();
				Bundle data = msg.getData();
				if (data != null) {
					String error = data.getString(Constants.LOGIN_ERROR);
					Toast.makeText(getApplicationContext(), error,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(getApplicationContext(), "CShWen失败",
							Toast.LENGTH_SHORT).show();
				break;
			case Constants.CONNECTING:
				showProgressDialog("CShWen连接中");
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		im = new ImManager(handler);
		uxml = new XmlUtil(getApplicationContext());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onDestroy() {
		im.exit();
		super.onDestroy();
	}

	public void loginClick(View v) {
		BootstrapEditText et_username = (BootstrapEditText) findViewById(R.id.login_username);
		BootstrapEditText et_pwd = (BootstrapEditText) findViewById(R.id.login_password);
		String username = et_username.getText().toString().trim();
		String pwd = et_pwd.getText().toString().trim();
		if (!Tool.isUserStandard(username))
			et_username.setError("请输入正确的帐号");
		else if (!Tool.isPwdStandard(pwd))
			et_pwd.setError("请输入正确的密码");
		else {
			im.loginUser(username, pwd);
			uxml.saveUserInfo(username, pwd);
		}
	}

	public void registerClick(View v) {
		im.exit();
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	public void showProgressDialog(String msg) {
		if (progress == null) {
			progress = new ProgressDialog(this);
		}
		if (progress.isShowing()) {
			progress.dismiss();
		}
		progress.setMessage(msg);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.show();
	}

	public void dismissProgressDialog() {
		if (progress != null && progress.isShowing()) {
			progress.dismiss();
		}
	}
}