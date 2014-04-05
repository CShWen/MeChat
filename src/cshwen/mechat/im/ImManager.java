/**
 * 
 */
package cshwen.mechat.im;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.ProviderManager;

import cshwen.mechat.utils.Constants;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @author CShWen
 * 
 */
public class ImManager {
	private static String TAG = ImManager.class.getSimpleName();
	private Handler mHandler;

	private static XMPPConnection connection;
	private static ConnectionConfiguration connConfig;
	/**
	 * 
	 */
	public ImManager(Handler handler) {
		this.mHandler = handler;
	}
	
	static{
		connConfig = new ConnectionConfiguration(Constants.MC_IP, Constants.MC_PORT);
		
		connection = new XMPPConnection(connConfig);

		ProviderManager pm = ProviderManager.getInstance();
//		configure(pm);
		//connConfig.setTruststorePath("/system/etc/security/cacerts.bks");
		//connConfig.setTruststoreType("bks");
	}
	public void loginUser(final String username, final String password) {
		new Thread() 
	     {
			@Override
			public void run() {
				/*若连接未建立，则先建立连接*/
				if(!connection.isConnected()){
					try {
						mHandler.sendEmptyMessage(Constants.CONNECTING);
						connection.connect();
					} catch (XMPPException e) {
						Log.e(TAG, e.toString());
						e.printStackTrace();
					}
				}
				
				/*如果当前是未登录状态，则进行登录认证，若 已经登录，则直接进行操作*/
				if(!connection.isAuthenticated()){
					mHandler.sendEmptyMessage(Constants.LOGINING);
					PacketFilter packetFilter = new PacketTypeFilter(IQ.class);
					PacketListener packetListener = new PacketListener() {
						@Override
						public void processPacket(Packet packet) {
							Log.e(TAG, packet.toXML());
							connection.removePacketListener(this);
							if(packet instanceof IQ){
								IQ response = (IQ) packet;
								/*登录失败，则提示失败信息*/
								if(response.getType() == IQ.Type.ERROR){
									XMPPError error = response.getError();
									String errorCondition = error.getCondition();
									Log.e(TAG, errorCondition);
									Bundle bundle = new Bundle();
									bundle.putString(Constants.LOGIN_ERROR, errorCondition);
									Message msg = new Message();
									msg.setData(bundle);
									msg.what = Constants.LOGIN_FAILURE;
									mHandler.sendMessage(msg);
								}else if(response.getType() == IQ.Type.RESULT){
									/*登录成功，跳转到消息发送页面*/
									Log.e(TAG, "登录成功，跳转到消息发送页面");
									mHandler.sendEmptyMessage(Constants.LOGINED);
								}
							}
						}
					};
					try {
						connection.login(username, password);
					} catch (XMPPException e) {
						Log.e(TAG, e.toString());
						String error = e.getMessage();
						/*如果是由于用户名未注册而登录失败，则重新注册该用户名*/
						if(error != null && error.contains("401")){
							Bundle bundle = new Bundle();
							bundle.putString(Constants.LOGIN_ERROR, error);
							Message msg = new Message();
							msg.setData(bundle);
							msg.what = Constants.LOGIN_FAILURE;
							mHandler.sendMessage(msg);
						}
					}
					connection.addPacketListener(packetListener, packetFilter);
				}else{
					mHandler.sendEmptyMessage(Constants.LOGINED);
					Log.e(TAG, "已经处于登录状态，跳转到消息发送页面");
				}
			}
		}.start();
	}
	
	public void registerUser(final String un, final String pwd) {
		new Thread() {
			@Override
			public void run() {
				/* 若连接未建立，则先建立连接 */
				if (!connection.isConnected()) {
					try {
						mHandler.sendEmptyMessage(Constants.CONNECTING);
						connection.connect();
					} catch (XMPPException e) {
						Log.e(TAG, e.toString());
						e.printStackTrace();
					}
				}
				mHandler.sendEmptyMessage(Constants.REGISTING);
				Registration registration = new Registration();
				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
						registration.getPacketID()), new PacketTypeFilter(
						IQ.class));
				PacketListener packetListener = new PacketListener() {
					@Override
					public void processPacket(Packet packet) {
						Log.e(TAG, packet.toXML());
						connection.removePacketListener(this);
						if (packet instanceof IQ) {
							IQ response = (IQ) packet;
							/* 注册失败，则提示失败信息 */
							if (response.getType() == IQ.Type.ERROR) {
								XMPPError error = response.getError();
								String errorCondition = error.getCondition();
								Log.e(TAG, errorCondition);
								Bundle bundle = new Bundle();
								bundle.putString(Constants.REGIST_ERROR,
										errorCondition);
								Message msg = new Message();
								msg.setData(bundle);
								msg.what = Constants.REGIST_FAILURE;
								mHandler.sendMessage(msg);
							} else if (response.getType() == IQ.Type.RESULT) {
								mHandler.sendEmptyMessage(Constants.REGISTED);
							}
						}
					}
				};
				connection.addPacketListener(packetListener, packetFilter);
				registration.setType(IQ.Type.SET);
				registration.addAttribute("username", un);
				registration.addAttribute("password", pwd);
				connection.sendPacket(registration);
			}
		}.start();
	}
	/**
	 * 
	 */
	public void exit() {
		connection.disconnect();
	}
	
}
