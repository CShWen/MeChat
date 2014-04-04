package cshwen.mechat.activity;

import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.Constants;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg){
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
		
		final ImManager im=new ImManager(handler);
		im.loginUser("user", "user");
		
		Button b=(Button)findViewById(R.id.login_login);
		b.setOnClickListener(new  OnClickListener() {
			@Override
			public void onClick(View arg0) {
				im.exit();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}