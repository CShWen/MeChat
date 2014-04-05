/**
 * 
 */
package cshwen.mechat.utils;

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
}
