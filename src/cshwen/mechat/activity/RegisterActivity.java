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
				System.out.println("CShWen��¼��");
				break;
			case Constants.LOGINED:
				System.out.println("CShWen�ѵ�¼");
				break;
			case Constants.LOGIN_FAILURE:
				System.out.println("CShWenʧ��");
				break;
			case Constants.CONNECTING:
				System.out.println("CShWen������");
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
			Toast.makeText(getApplicationContext(), "�������벻һ��",
					Toast.LENGTH_LONG).show();
		else if (!Tool.isUserStandard(username))
			register_username.setError("������淶���ʺ�");
		else if (!Tool.isPwdStandard(pwd))
			register_pwd.setError("������淶������");
		else
			im.registerUser(username, pwd);
	}
}
