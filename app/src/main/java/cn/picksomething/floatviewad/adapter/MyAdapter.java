package cn.picksomething.floatviewad.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.picksomething.floatviewad.R;
import cn.picksomething.floatviewad.model.AdItem;

/**
 * Created by caobin on 15-2-5.
 */
public class MyAdapter extends BaseAdapter {

    private static final String TAG = "MyAdapter";

    private Context mContext;
    private ArrayList<AdItem> mAdItem;
    private int mResourceId;

    public MyAdapter(Context context, int layoutID, ArrayList<AdItem> adData) {
        mContext = context;
        mResourceId = layoutID;
        mAdItem = adData;
    }

    @Override
    public int getCount() {
        return mAdItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "position " + position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mResourceId, null);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.adItemImage);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.adItemTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mImageView.setImageResource(mAdItem.get(position).getmImageId());
        viewHolder.mTitle.setText(mAdItem.get(position).getmTitle());

        return convertView;
    }

    final class ViewHolder {
        private ImageView mImageView;
        private TextView mTitle;
    }
}
