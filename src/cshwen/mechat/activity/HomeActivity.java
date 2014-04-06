/**
 * 
 */
package cshwen.mechat.activity;

import cshwen.mechat.im.ImManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author CShWen
 * 
 */
public class HomeActivity extends Activity {

	ImManager im;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
		}
	};
	/**
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		im = new ImManager(handler);
//		im.searchUser("test");
//		im.addFriend("test@cshwen","sb"); // ҪJID,�ǳƿ�ѡ
		im.showFriends();
	}

}
