/**
 * 
 */
package cshwen.mechat.utils;

import java.io.Serializable;

/**
 * @author CShWen
 * 
 */
public class Msg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String fromName;
	private String toName;
	private boolean isIn;
	private String content;
	private String fromTime;

	/**
	 * 
	 */
	public Msg() {
		// TODO 自动生成的构造函数存根
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public boolean isIn() {
		return isIn;
	}

	public void setIn(boolean isIn) {
		this.isIn = isIn;
	}
}
