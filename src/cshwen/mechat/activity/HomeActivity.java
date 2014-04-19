/**
 * 
 */
package cshwen.mechat.activity;

import cshwen.mechat.adapter.FriendAdapter;
import cshwen.mechat.im.ImManager;
import cshwen.mechat.service.BackService;
import cshwen.mechat.utils.Constants;
import cshwen.mechat.utils.FriendClass;
import cshwen.mechat.utils.XmlUtil;
import cshwen.mechat.utils.Tool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
	XmlUtil uxml;
	NotificationManager notificationManager;
	AlertDialog tellDialog;
	ImManager im;
	ListView listFriends;
	Handler handler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			final String from_JID = (String) msg.obj;
			final String from_user = from_JID.substring(0, from_JID.indexOf("@"));
			switch (msg.what) {
			case Constants.FRIEND_APPLY:
				Toast.makeText(getApplicationContext(), "好友申请",
						Toast.LENGTH_LONG).show();
				tellDialog = new AlertDialog.Builder(HomeActivity.this).setTitle("好友申请")
						.setMessage(from_user + "请求添加您为好友？").setPositiveButton("同意",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										showFriends();
									}
								}).setNegativeButton("拒绝", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface arg0, int arg1) {
										im.delFriend(from_JID);
										showFriends();
									}}).show();
				break;
			case Constants.FRIEND_AGREE:
				showFriends();
				if (tellDialog!=null&&tellDialog.isShowing())
					tellDialog.dismiss();
				Toast.makeText(getApplicationContext(), "申请同意", Toast.LENGTH_LONG).show();
				Intent agreeIntent = new Intent(HomeActivity.this, ChatActivity.class);
				agreeIntent.putExtra("toUserJID", from_JID);
				agreeIntent.putExtra("toUserName", from_user);
				PendingIntent p_agreeIntent = PendingIntent.getActivity(
						HomeActivity.this, 0, agreeIntent, 0);
				Notification agree_ntf = new Notification(
						R.drawable.ic_launcher, "好友申请通过", System.currentTimeMillis());
				agree_ntf.defaults = Notification.DEFAULT_SOUND;
				agree_ntf.setLatestEventInfo(HomeActivity.this, (String) "好友申请通过", from_user + "同意加您为好友", p_agreeIntent);
				notificationManager.notify(from_user, Tool.getHashNum(from_user), agree_ntf);
				break;
			case Constants.FRIEND_REFUSE:
				Toast.makeText(getApplicationContext(), from_user + "拒绝成为您的好友",
						Toast.LENGTH_LONG).show();
				break;
			case Constants.FRIEND_STOP:
				Toast.makeText(getApplicationContext(), from_user + "与您断绝好友关系",
						Toast.LENGTH_LONG).show();
				notificationManager.cancel(from_user, Tool.getHashNum(from_user));
				showFriends();
				break;
			}
		}
	};

	BackService.CBinder binder;
	private ServiceConnection conn = new ServiceConnection() {
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			binder = (BackService.CBinder) arg1;
		}

		public void onServiceDisconnected(ComponentName arg0) {

		}
	};

	/**
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		im = ImManager.getInstance();
		im.setHandler(handler);
		im.contactsListen();
		listFriends = (ListView) findViewById(R.id.home_friends);
		showFriends();
		// im.addFriend("test@cshwen","sb"); // 要JID,昵称可选
		// im.delFriend("test@cshwen");
		// im.isPresence("test@cshwen");
		uxml = new XmlUtil(getApplicationContext());
//		Intent service = new Intent(HomeActivity.this, BackService.class);
//		service.putExtra("user", uxml.getUserName());
//		service.putExtra("ChWenMeChatMsg", new Messenger(handler));
//		bindService(service, conn, Context.BIND_AUTO_CREATE);

		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

	public void onDestroy() {
		im.exit();
		im = null;
		notificationManager.cancelAll();
		super.onDestroy();
	}

	private void showFriends() { // 显示好友
		listFriends.setAdapter(new FriendAdapter(getApplicationContext(), im
				.showFriends()));
		listFriends.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				FriendClass fc = (FriendClass) listFriends
						.getItemAtPosition(pos);
				Toast.makeText(getApplicationContext(), "点击：" + fc.getJID(),
						Toast.LENGTH_LONG).show();
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
										im.addFriend(fc.getJID(), fc.getJID());
									}
								}).setNegativeButton("取消", null).show();
			}
		});
		listFriends.setOnItemLongClickListener(null);
	}
}
