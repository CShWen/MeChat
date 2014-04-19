/**
 * 
 */
package cshwen.mechat.activity;

import java.util.ArrayList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import cshwen.mechat.adapter.ChatAdapter;
import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.Constants;
import cshwen.mechat.utils.Msg;
import cshwen.mechat.utils.Tool;
import cshwen.mechat.utils.XmlUtil;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author CShWen
 * 
 */
public class ChatActivity extends Activity {
	String toUserJID;
	EditText chat_text;
	XmlUtil xmlul;
	ImManager im;
	ChatAdapter cAdapter;
	Chat oneChat;
	private ChatManager chatManager;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Msg sendMsg = (Msg) msg.obj;
			switch (msg.what) {
			case Constants.MSG_RECEIVER:// /接受
				cAdapter.getMsgList().add(sendMsg);
				cAdapter.notifyDataSetChanged();
				break;
			case Constants.MSG_SEND:// /发送
				cAdapter.getMsgList().add(sendMsg);
				cAdapter.notifyDataSetChanged();
				break;
			}
		};
	};

	/**
	 * 
	 */
	public ChatActivity() {
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		Intent intent = getIntent();
		toUserJID = intent.getStringExtra("toUserJID");
		im=ImManager.getInstance();
		im.setHandler(mHandler);
		chatManager = im.getConnection().getChatManager();
		oneChat = chatManager.createChat(toUserJID, new MessageListener() {
			public void processMessage(Chat ct, Message msg) {
				Log.e("Message", msg.getFrom() + ":" + msg.getBody());
				Msg receiverMsg = new cshwen.mechat.utils.Msg();
				receiverMsg.setFromName(msg.getFrom());
				receiverMsg.setContent(msg.getBody());
				receiverMsg.setFromTime(Tool.getNowTime());
				receiverMsg.setIn(true);
				
				android.os.Message tMsg = mHandler.obtainMessage();
				tMsg.what = Constants.MSG_RECEIVER;
				tMsg.obj = receiverMsg;
				tMsg.sendToTarget();
			}
		});
		xmlul = new XmlUtil(getApplicationContext());
		chat_text = (EditText) findViewById(R.id.chat_text);

		ListView chat_messages = (ListView) findViewById(R.id.chat_messages);
		cAdapter = new ChatAdapter(getApplicationContext(),
				new ArrayList<Msg>());
		chat_messages.setAdapter(cAdapter);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(intent.getStringExtra("toUserName"));
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
	
	public void sendClick(View v) {
		String msg = chat_text.getText().toString().trim();
		if (msg != null && !msg.equals("")) {
			if (oneChat != null) {
				try {
					Message message = new Message();
					message.setTo(xmlul.getUserName() + "@cshwen");
					message.setBody(msg);
					message.setFrom(toUserJID);
					message.setType(Type.chat);
					oneChat.sendMessage(message);

					Msg sendMsg = new Msg();
					sendMsg.setFromTime(Tool.getNowTime());
					sendMsg.setFromName(xmlul.getUserName());
					sendMsg.setIn(false);
					sendMsg.setContent(msg);

					android.os.Message bundle = mHandler.obtainMessage();
					bundle.what = Constants.MSG_SEND;
					bundle.obj = sendMsg;
					bundle.sendToTarget();

					chat_text.setText("");
				} catch (XMPPException e) {
					Log.e("Message", e.toString());
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getApplicationContext(), "连接异常，不能发送数据",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "发送内容为空！",
					Toast.LENGTH_SHORT).show();
		}
	}
}
