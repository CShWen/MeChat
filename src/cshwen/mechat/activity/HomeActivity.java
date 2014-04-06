/**
 * 
 */
package cshwen.mechat.activity;

import cshwen.mechat.adapter.FriendAdapter;
import cshwen.mechat.im.ImManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

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
		ListView listFriends=(ListView)findViewById(R.id.home_friends);
		listFriends.setAdapter(new FriendAdapter(getApplicationContext(), im.showFriends()));
//		im.searchUser("test");
//		im.addFriend("test@cshwen","sb"); // ҪJID,�ǳƿ�ѡ
//		im.showFriends();
//		im.delFriend("test@cshwen");
//		im.isPresence("test@cshwen");
	}

}
