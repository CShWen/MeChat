/**
 * 
 */
package cshwen.mechat.activity;

import cshwen.mechat.adapter.FriendAdapter;
import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.FriendClass;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author CShWen
 * 
 */
public class HomeActivity extends Activity {

	ImManager im;
	ListView listFriends;
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
		listFriends = (ListView) findViewById(R.id.home_friends);
		listFriends.setAdapter(new FriendAdapter(getApplicationContext(), im
				.showFriends()));
		// im.searchUser("test");
		// im.addFriend("test@cshwen","sb"); // 要JID,昵称可选
		// im.showFriends();
		// im.delFriend("test@cshwen");
		// im.isPresence("test@cshwen");
		listFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				FriendClass fc = (FriendClass) listFriends
						.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(), "点击："+fc.getUsername(), Toast.LENGTH_LONG).show();
				Intent intent=new Intent(HomeActivity.this,ChatActivity.class);
				intent.putExtra("toUserJID", fc.getUsername());
				startActivity(intent);
			}
		});
		listFriends.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				FriendClass fc = (FriendClass) listFriends
						.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(), "长按："+fc.getUsername(), Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}

}
