package cn.picksomething.floatviewad.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import cn.picksomething.floatviewad.FloatAdApplication;
import cn.picksomething.floatviewad.R;
import cn.picksomething.floatviewad.views.FloatFunctionView;

/**
 * Created by caobin on 15-2-5.
 */
public class FloatFunctionAdService extends Service {

    private static final String TAG = "FloatFunctionAdService";

    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mWindowManagerParams = null;
    private FloatFunctionView mFloatFunctionView = null;
    private Handler mHandler = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("caobin", "onStartCommand");
        initDatas();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                createFloatFunctionAd();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void initDatas() {
        mHandler = new Handler();
    }

    private void createFloatFunctionAd() {
        mFloatFunctionView = new FloatFunctionView(getApplicationContext());
        mFloatFunctionView.setOnClickListener(new MyClickListener());
        mFloatFunctionView.setText(R.string.float_function_ad_text);
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManagerParams = ((FloatAdApplication) getApplication()).getWindowParams();
        mWindowManager.addView(mFloatFunctionView, mWindowManagerParams);
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            createFloatAdList();
            mWindowManager.removeView(mFloatFunctionView);
        }
    }

    private void createFloatAdList() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View adList = inflater.inflate(R.layout.float_ad_list, null);
        mWindowManager.addView(adList, createAdListParams());
    }

    private WindowManager.LayoutParams createAdListParams() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManagerParams = ((FloatAdApplication) getApplication()).getWindowParams();
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mWindowManagerParams.height = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.float_list_height);
        mWindowManagerParams.width = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.float_list_width);
        mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManagerParams.gravity = Gravity.CENTER;
        mWindowManagerParams.x = 0;
        mWindowManagerParams.y = 0;
        return mWindowManagerParams;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
