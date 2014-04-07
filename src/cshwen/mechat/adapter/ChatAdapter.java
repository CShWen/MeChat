/**
 * 
 */
package cshwen.mechat.adapter;

import java.util.List;

import cshwen.mechat.activity.R;
import cshwen.mechat.utils.Msg;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author CShWen
 * 
 */
public class ChatAdapter extends BaseAdapter {

	private Context context;
	private List<Msg> listData;

	/**
	 * 
	 */
	public ChatAdapter(Context context, List<Msg> listData) {
		this.context = context;
		this.listData = listData;
	}

	public List<Msg> getMsgList() {
		return listData;
	}

	public void setMsgList(List<Msg> listData) {
		this.listData = listData;
	}

	public int getCount() {
		return listData.size();
	}

	public Object getItem(int arg0) {
		return listData.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup arg2) {
		Msg msg = listData.get(position);

		ViewHolder holder;
		if (msg.isIn()) {
			convertView = LinearLayout.inflate(context, R.layout.item_chat_in,
					null);
			holder = new ViewHolder();
			holder.chat_head = (TextView) convertView
					.findViewById(R.id.in_head);
			holder.chat_content = (TextView) convertView
					.findViewById(R.id.in_content);
		} else {
			convertView = LinearLayout.inflate(context, R.layout.item_chat_out,
					null);
			holder = new ViewHolder();
			holder.chat_head = (TextView) convertView
					.findViewById(R.id.out_head);
			holder.chat_content = (TextView) convertView
					.findViewById(R.id.out_content);
		}
		holder.chat_head.setText(msg.getFromName() + "  " + msg.getFromTime());
		holder.chat_content.setText(msg.getContent());
		return convertView;
	}

	static class ViewHolder {
		TextView chat_head;
		TextView chat_content;
	}

}
