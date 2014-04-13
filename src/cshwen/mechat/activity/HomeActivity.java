/**
 * 
 */
package cshwen.mechat.activity;

import cshwen.mechat.adapter.FriendAdapter;
import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.FriendClass;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
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
		showFriends();
		// im.addFriend("test@cshwen","sb"); // 要JID,昵称可选
		// im.delFriend("test@cshwen");
		// im.isPresence("test@cshwen");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_friend:
			showFriends();
			return true;
		case R.id.action_search:
			findContacts();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showFriends() { // 显示好友
		listFriends.setAdapter(new FriendAdapter(getApplicationContext(), im
				.showFriends()));
		listFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				FriendClass fc = (FriendClass) listFriends
						.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(),
						"点击：" + fc.getJID(), Toast.LENGTH_LONG).show();
				Intent intent = new Intent(HomeActivity.this,
						ChatActivity.class);
				intent.putExtra("toUserJID", fc.getJID());
				intent.putExtra("toUserName", fc.getName());
				startActivity(intent);
			}
		});
		listFriends.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				FriendClass fc = (FriendClass) listFriends
						.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(),
						"长按：" + fc.getUsername(), Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}
	
	private void findContacts() {
		final EditText et = new EditText(this);
		new AlertDialog.Builder(this).setTitle("找人").setView(et)
				.setPositiveButton("查找", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						String str = et.getText().toString().trim();
						if (!str.equals(""))
							listFriends.setAdapter(new FriendAdapter(
									getApplicationContext(), im.searchUser(str)));
						else
							Toast.makeText(getApplicationContext(), "不允许搜索空！",
									Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("取消", null).show();
		listFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				final FriendClass fc = (FriendClass) listFriends
						.getItemAtPosition(pos);

				new AlertDialog.Builder(HomeActivity.this)
						.setTitle("添加好友")
						.setMessage("是否发送请求添加 " + fc.getUsername() + " 为您的好友？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
//										im.addFriend("test@cshwen","自定义昵称");
										im.addFriend(fc.getJID(),null);
									}
								}).setNegativeButton("取消", null).show();
			}
		});
		listFriends.setOnItemLongClickListener(null);
	}
}
