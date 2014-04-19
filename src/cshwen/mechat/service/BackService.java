/**
 * 
 */
package cshwen.mechat.service;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import cshwen.mechat.im.ImManager;
import cshwen.mechat.utils.Constants;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author CShWen
 * 
 */
public class BackService extends Service {

	private CBinder binder = new CBinder();
	ImManager im;
	Messenger messenger;
	String username;
	
	public class CBinder extends Binder {
	}

	public IBinder onBind(Intent intent) {
		Bundle bundle = intent.getExtras();
		messenger = (Messenger) bundle.get("ChWenMeChatMsg");
		username=bundle.getString("user");
		return binder;
	}

	public void onCreate() {
		im = ImManager.getInstance();
		im.contactsListen();
//		PacketFilter filter = new AndFilter(
//				new PacketTypeFilter(Presence.class));
//		PacketListener listener = new PacketListener() {
//
//			public void processPacket(Packet packet) {
//				Log.i("Presence", "PresenceService------" + packet.toXML());
//				if (packet.getFrom().contains(username)) {
//					return;
//				}
//				if (packet instanceof Presence) {
//					Log.i("Presence", packet.toXML());
//					Presence presence = (Presence) packet;
//					String from = presence.getFrom();// 发送方
//					String to = presence.getTo();// 接收方
//					// Presence.Type有7中状态
//					System.out.println("[" + from + "]->[" + to + "]?");
//					Message msg = Message.obtain();
//					msg.obj = from;
//					if (presence.getType().equals(Presence.Type.subscribe)) {// 好友申请
////						im.testMsg();
////						if(!im.isExistUser(from)){
//							msg.what = Constants.FRIEND_APPLY;
//							try {
//								messenger.send(msg);
//							} catch (RemoteException e) {
//								e.printStackTrace();
//							}
//							System.out.println("CShWen，请求成为你的好友");
////						}
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.subscribed)) {// 同意添加好友
//						msg.what = Constants.FRIEND_AGREE;
//						try {
//							messenger.send(msg);
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//						System.out.println("CShWen，同意加你好友__OK");
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.unsubscribe)) {// 拒绝添加好友 和 删除好友
//						msg.what = Constants.FRIEND_REFUSE;
//						try {
//							messenger.send(msg);
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//						System.out.println("CShWen，拒绝成为您的好友");
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.unsubscribed)) {// 这个我没用到
//						msg.what = Constants.FRIEND_STOP;
//						try {
//							messenger.send(msg);
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//						System.out.println("CShWen，与您断绝好友关系__OK");
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.unavailable)) {// 好友下线
//															// 要更新好友列表，可以在这收到包后，发广播到指定页面
//															// 更新列表
//					} else {// 好友上线
//
//					}
//				}
//			}
//		};
//		im.setMsgFilter(listener, filter);
		super.onCreate();
	}

	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public void onDestroy() {
		super.onDestroy();
	}

}
