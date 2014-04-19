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
//					String from = presence.getFrom();// ���ͷ�
//					String to = presence.getTo();// ���շ�
//					// Presence.Type��7��״̬
//					System.out.println("[" + from + "]->[" + to + "]?");
//					Message msg = Message.obtain();
//					msg.obj = from;
//					if (presence.getType().equals(Presence.Type.subscribe)) {// ��������
////						im.testMsg();
////						if(!im.isExistUser(from)){
//							msg.what = Constants.FRIEND_APPLY;
//							try {
//								messenger.send(msg);
//							} catch (RemoteException e) {
//								e.printStackTrace();
//							}
//							System.out.println("CShWen�������Ϊ��ĺ���");
////						}
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.subscribed)) {// ͬ����Ӻ���
//						msg.what = Constants.FRIEND_AGREE;
//						try {
//							messenger.send(msg);
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//						System.out.println("CShWen��ͬ��������__OK");
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.unsubscribe)) {// �ܾ���Ӻ��� �� ɾ������
//						msg.what = Constants.FRIEND_REFUSE;
//						try {
//							messenger.send(msg);
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//						System.out.println("CShWen���ܾ���Ϊ���ĺ���");
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.unsubscribed)) {// �����û�õ�
//						msg.what = Constants.FRIEND_STOP;
//						try {
//							messenger.send(msg);
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//						System.out.println("CShWen�������Ͼ����ѹ�ϵ__OK");
//						return;
//					} else if (presence.getType().equals(
//							Presence.Type.unavailable)) {// ��������
//															// Ҫ���º����б����������յ����󣬷��㲥��ָ��ҳ��
//															// �����б�
//					} else {// ��������
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
