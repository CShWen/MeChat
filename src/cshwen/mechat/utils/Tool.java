/**
 * 
 */
package cshwen.mechat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CShWen
 *
 */
public class Tool {

	/**
	 * 
	 */
	public Tool() {
		// TODO 自动生成的构造函数存根
	}

	public static boolean isUserStandard(String str) { // 不为空为真
		return str.matches("^[a-zA-Z0-9_]{3,20}$");
	}

	public static boolean isPwdStandard(String str) { // 不为空为真
		return str.matches("^[A-Za-z0-9~!@#$%^&*(){};<>,.?/'_+=-]{3,20}$");
	}
	

	public static String getNowTime(){
		SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm");
		return df.format(new Date());
	}
	
	public static int getHashNum(String key) {
        int hash, i;
        for (hash = key.length(), i = 0; i < key.length(); ++i)
            hash = (hash << 4) ^ (hash >> 28) ^ key.charAt(i);
        return (hash % 317);
    }
}
