/**
 * 
 */
package cshwen.mechat.utils;

/**
 * @author CShWen
 * 
 */
public class FriendClass {
	private String JID;
	private String username;
	private String name;
	private String email;

	/**
	 * 
	 */
	public FriendClass(String JID, String username, String name, String email) {
		this.JID = JID;
		this.username = username;
		if (username == null || username.equals(""))
			this.username = JID.substring(0, JID.indexOf("@"));
		else
			this.username = username;
		if (name == null || name.equals(""))
			this.name = JID.substring(0, JID.indexOf("@"));
		else
			this.name = name;
		this.email = email;
	}

	public String getUsername() {
		return this.username;
	}

	public String getName() {
		return this.name;
	}

	public String getJID() {
		return this.JID;
	}

	public String getEmail() {
		return this.email;
	}
}
