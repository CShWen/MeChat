/**
 * 
 */
package cshwen.mechat.im;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
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
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.ReportedData.Row;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;

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

	static {
		connConfig = new ConnectionConfiguration(Constants.MC_IP,
				Constants.MC_PORT);

		connection = new XMPPConnection(connConfig);

		ProviderManager pm = ProviderManager.getInstance();
		configure(pm);
		// connConfig.setTruststorePath("/system/etc/security/cacerts.bks");
		// connConfig.setTruststoreType("bks");
	}

	public void loginUser(final String username, final String password) {
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

				/* 如果当前是未登录状态，则进行登录认证，若 已经登录，则直接进行操作 */
				if (!connection.isAuthenticated()) {
					mHandler.sendEmptyMessage(Constants.LOGINING);
					PacketFilter packetFilter = new PacketTypeFilter(IQ.class);
					PacketListener packetListener = new PacketListener() {
						@Override
						public void processPacket(Packet packet) {
							Log.e(TAG, packet.toXML());
							connection.removePacketListener(this);
							if (packet instanceof IQ) {
								IQ response = (IQ) packet;
								/* 登录失败，则提示失败信息 */
								if (response.getType() == IQ.Type.ERROR) {
									XMPPError error = response.getError();
									String errorCondition = error
											.getCondition();
									Log.e(TAG, errorCondition);
									Bundle bundle = new Bundle();
									bundle.putString(Constants.LOGIN_ERROR,
											errorCondition);
									Message msg = new Message();
									msg.setData(bundle);
									msg.what = Constants.LOGIN_FAILURE;
									mHandler.sendMessage(msg);
								} else if (response.getType() == IQ.Type.RESULT) {
									/* 登录成功，跳转到消息发送页面 */
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
						/* 如果是由于用户名未注册而登录失败，则重新注册该用户名 */
						if (error != null && error.contains("401")) {
							Bundle bundle = new Bundle();
							bundle.putString(Constants.LOGIN_ERROR, error);
							Message msg = new Message();
							msg.setData(bundle);
							msg.what = Constants.LOGIN_FAILURE;
							mHandler.sendMessage(msg);
						}
					}
					connection.addPacketListener(packetListener, packetFilter);
				} else {
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

	public XMPPConnection getConnection(){
		if(!connection.isConnected()){
			try {
				connection.connect();
			} catch (XMPPException e) {
				Log.e(TAG, e.toString());
				e.printStackTrace();
			}
		}
		return connection;
	}

	public void searchUser(String searchName){
		try{
			UserSearchManager search = new UserSearchManager(getConnection());
			Form searchForm = search.getSearchForm("search."+Constants.MC_SERVER);
			Form answerForm = searchForm.createAnswerForm();
			answerForm.setAnswer("Username", true);
			answerForm.setAnswer("search", searchName);
			ReportedData data = search.getSearchResults(answerForm,"search."+Constants.MC_SERVER);
			
			Iterator<Row> it = data.getRows();
			Row row=null;
			while(it.hasNext()){
				String ansS="";
				row=it.next();
//				ansS+=row.getValues("Username").next().toString()+"\n";
				
				//Log.i("Username",row.getValues("Username").next().toString());
				System.out.println("[csw测试的]:"+row.getValues("Username").next()+"|||"+row.getValues("Name").next()+"|||"+row.getValues("Email").next()+"|||"+row.getValues("JID").next());
			}
//			Toast.makeText(this,ansS, Toast.LENGTH_SHORT).show();
		}catch(Exception e){
			System.out.println("[csw异常]:"+e.getClass().toString());
			e.printStackTrace();
		}
	}
	
	public void addFriend(String addJID, String nick) {
		Roster roster = getConnection().getRoster();
		try {
//			roster.createEntry(addName, null, new String[] { "friends" });
			roster.createEntry(addJID, nick, null);
			System.out.println("[cshwen添加好友success]");
		} catch (XMPPException e) {
			System.out.println("[cshwen添加好友Error]");
			e.printStackTrace();
		}
	}
	
	public void showFriends(){
		Roster roster=getConnection().getRoster();
		Collection<RosterEntry> it = roster.getEntries();
		ArrayList<String> friends = new ArrayList<String>();
		for(RosterEntry rosterEnter:it){
			friends.add(rosterEnter.getUser());
			// getUser为JID，getName为自定义昵称
			System.out.println("[cshwen|it好友：]"+rosterEnter.getUser()+"{|}"+rosterEnter.getName());
		}
	
		if (friends.size()==0){
			friends.add("You have no friend");
		}
//		HashMap hm=new HashMap();  
//        Collection<RosterEntry> m=roster.getEntries();  
//        for(Iterator<RosterEntry> i=m.iterator();i.hasNext();){  
//            RosterEntry re= i.next();  
//            System.out.println("name:"+re.getName());//打印好友信息  
//            System.out.println("user:"+re.getUser());  
//              
//        }  
	}
	
	public static void configure(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());

		// pm.addIQProvider("open", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Open());
		//
		// pm.addIQProvider("close", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Close());
		//
		// pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
		// new IBBProviders.Data());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());

		pm.addIQProvider("command", "http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider());
		pm.addExtensionProvider("malformed-action",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.MalformedActionError());
		pm.addExtensionProvider("bad-locale",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadLocaleError());
		pm.addExtensionProvider("bad-payload",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadPayloadError());
		pm.addExtensionProvider("bad-sessionid",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.BadSessionIDError());
		pm.addExtensionProvider("session-expired",
				"http://jabber.org/protocol/commands",
				new AdHocCommandDataProvider.SessionExpiredError());
	}
}
