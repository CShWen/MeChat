package cshwen.mechat.activity;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.Constants;
import cshwen.mechat.utils.Tool;
import cshwen.mechat.utils.XmlUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	ImManager im;
	XmlUtil uxml;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.LOGINING:
				System.out.println("CShWen登录中");
				break;
			case Constants.LOGINED:
				System.out.println("CShWen已登录");
				break;
			case Constants.LOGIN_FAILURE:
				System.out.println("CShWen失败");
				break;
			case Constants.CONNECTING:
				System.out.println("CShWen连接中");
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

}