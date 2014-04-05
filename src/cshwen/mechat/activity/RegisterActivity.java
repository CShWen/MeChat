/**
 * 
 */
package cshwen.mechat.activity;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.Constants;
import cshwen.mechat.utils.Tool;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

/**
 * @author CShWen
 * 
 */
public class RegisterActivity extends Activity {
	ImManager im;
	Handler handler = new Handler() {
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

	/**
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		im = new ImManager(handler);
		final FontAwesomeText register_rule = (FontAwesomeText) findViewById(R.id.register_rule);
		register_rule.startFlashing(this, true, FontAwesomeText.AnimationSpeed.MEDIUM);
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
}
