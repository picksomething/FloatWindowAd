package cn.picksomething.floatviewad.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cn.picksomething.floatviewad.MyWindowManager;
import cn.picksomething.floatviewad.R;

/**
 * Created by caobin on 15-2-4.
 */
public class FloatFunctionAdView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;

    public FloatFunctionAdView(final Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_function_ad_view, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDownloadList();
            }

            private void showDownloadList() {
                MyWindowManager.createDownloadList();
            }
        });
    }
}
