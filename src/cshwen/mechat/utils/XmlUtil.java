/**
 * 
 */
package cshwen.mechat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author CShWen
 * 
 */
public class XmlUtil {

	private SharedPreferences sp;

	/**
	 * 
	 */
	public XmlUtil(Context context) {
		sp = context.getSharedPreferences(Constants.xmlKey,
				Context.MODE_PRIVATE);
	}

	public void saveUserInfo(String un, String pwd) {
		Editor edt = sp.edit();
		edt.putString("username", un);
		edt.putString("pwd", pwd);
		edt.commit();
	}

	public String getUserName() {
		return sp.getString("username", null);
	}

	public String getUserPwd() {
		return sp.getString("pwd", null);
	}
}
