/**
 * 
 */
package cshwen.mechat.activity;

import org.jivesoftware.smack.packet.XMPPError;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.Constants;
import cshwen.mechat.utils.Tool;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * @author CShWen
 * 
 */
public class RegisterActivity extends Activity {
	ProgressDialog progress;
	ImManager im;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constants.REGISTING:
				showProgressDialog("CShWen注册中");
				break;
			case Constants.REGISTED:
				dismissProgressDialog();
				Toast.makeText(getApplicationContext(), "CShWen注册成功",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(RegisterActivity.this,
						HomeActivity.class);
				startActivity(intent);
				break;
			case Constants.REGIST_FAILURE:
				dismissProgressDialog();
				Bundle data = msg.getData();
				if (data != null) {
					String error = data.getString(Constants.REGIST_ERROR);
					if (error.equals(XMPPError.Condition.conflict.toString())) {
						Toast.makeText(getApplicationContext(), "该用户已存在，请直接登录",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), error,
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case Constants.CONNECTING:
				showProgressDialog("CShWen连接中");
				break;
			}
		}
	};

	/**
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		im=ImManager.getInstance();
		im.setHandler(handler);
		final FontAwesomeText register_rule = (FontAwesomeText) findViewById(R.id.register_rule);
		register_rule.startFlashing(this, true,
				FontAwesomeText.AnimationSpeed.MEDIUM);

		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public boolean onOptionsItemSelected(MenuItem mi) {
		if (mi.isCheckable()) {
			mi.setChecked(true);
		}
		switch (mi.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	public void registerClick(View v) {
		BootstrapEditText register_username = (BootstrapEditText) findViewById(R.id.register_username);
		BootstrapEditText register_pwd = (BootstrapEditText) findViewById(R.id.register_pwd);
		BootstrapEditText register_2pwd = (BootstrapEditText) findViewById(R.id.register_2pwd);
		String username = register_username.getText().toString().trim();
		String pwd = register_pwd.getText().toString().trim();
		String pwd2 = register_2pwd.getText().toString().trim();
		if (!pwd.equals(pwd2))
			Toast.makeText(getApplicationContext(), "上下密码不一致",
					Toast.LENGTH_LONG).show();
		else if (!Tool.isUserStandard(username))
			register_username.setError("请输入规范的帐号");
		else if (!Tool.isPwdStandard(pwd))
			register_pwd.setError("请输入规范的密码");
		else
			im.registerUser(username, pwd);
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
