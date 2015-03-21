package cn.picksomething.floatviewad.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.picksomething.floatviewad.R;
import cn.picksomething.floatviewad.model.AdItem;
import cn.picksomething.floatviewad.utils.DownloadFileUtils;

/**
 * Created by caobin on 15-2-5.
 */
public class MyAdapter extends BaseAdapter {

    private static final String TAG = "MyAdapter";

    private WindowManager mWindowManager = null;
    private Context mContext;
    private ArrayList<AdItem> mAdItem;
    private int mResourceId;
    private LinearLayout mLinearLayout;

    public MyAdapter(Context context, int layoutID, ArrayList<AdItem> adData, LinearLayout linearLayout) {
        mContext = context;
        mResourceId = layoutID;
        mAdItem = adData;
        mLinearLayout = linearLayout;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "position " + position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(mResourceId, null);
            viewHolder.mConvertView = convertView;
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.adItemImage);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.adItemTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mConvertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "You Clicked Item " + position);
                String apkName = mAdItem.get(position).getmTitle();
                Uri uri = Uri.parse(mAdItem.get(position).getmUri());
                Log.d(TAG, "Uri is " + uri);
                DownloadFileUtils downloadFileUtils = new DownloadFileUtils(mContext, uri, apkName);
                downloadFileUtils.startDownloadAd();
                mWindowManager.removeView(mLinearLayout);
            }
        });

        viewHolder.mImageView.setImageResource(mAdItem.get(position).getmImageId());
        viewHolder.mTitle.setText(mAdItem.get(position).getmTitle());

        return convertView;
    }

    final class ViewHolder {
        private View mConvertView;
        private ImageView mImageView;
        private TextView mTitle;
    }
}
