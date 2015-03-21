package cn.picksomething.floatviewad.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import cn.picksomething.floatviewad.FloatAdApplication;
import cn.picksomething.floatviewad.R;
import cn.picksomething.floatviewad.utils.DownloadFileUtils;
import cn.picksomething.floatviewad.views.FloatIconView;


/**
 * Created by caobin on 15-2-4.
 */
public class FloatIconAdService extends Service {

    private static final String TAG = "FloatIconAdService";

    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mWindowManagerParams = null;
    private FloatIconView mFloatIconView = null;
    private Handler mHandler = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initDatas();
        Log.d(TAG, "onStartCommand");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                createFloatIconAd();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void initDatas() {
        mHandler = new Handler();
    }

    private void createFloatIconAd() {
        mFloatIconView = new FloatIconView(getApplicationContext());
        mFloatIconView.setOnClickListener(new MyClickListener());
        mFloatIconView.setImageResource(R.drawable.ic_new);
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManagerParams = ((FloatAdApplication) getApplication()).getWindowParams();
        mWindowManager.addView(mFloatIconView, mWindowManagerParams);
    }

    private class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse("http://gdown.baidu.com/data/wisegame/c922005f8e9a51a0/wangyiyouxiang_53.apk");
            String apkName = "网易邮箱";
            DownloadFileUtils downloadFileUtils = new DownloadFileUtils(getApplicationContext(), uri, apkName);
            downloadFileUtils.startDownloadAd();
            mWindowManager.removeViewImmediate(mFloatIconView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
