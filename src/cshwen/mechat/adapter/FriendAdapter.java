/**
 * 
 */
package cshwen.mechat.adapter;

import java.util.ArrayList;

import cshwen.mechat.activity.HomeActivity;
import cshwen.mechat.activity.R;
import cshwen.mechat.utils.FriendClass;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author CShWen
 * 
 */
public class FriendAdapter extends BaseAdapter {
	private ArrayList<FriendClass> listData;
	private LayoutInflater layoutInflater;

	/**
	 * 
	 */
	public FriendAdapter(Context context, ArrayList<FriendClass> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return listData.size();
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return listData.get(arg0);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_friend, null);
			holder = new ViewHolder();
			holder.View_item_username = (TextView) convertView
					.findViewById(R.id.friend_username);
			holder.View_item_photo=(ImageView)convertView.findViewById(R.id.friend_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.View_item_username.setText(listData.get(position).getUsername());
		holder.View_item_photo.setImageResource(HomeActivity.headImgIds[(int) (Math.random()*9)]);
		return convertView;
	}

	static class ViewHolder {
		TextView View_item_username;
		ImageView View_item_photo;
	}
}
