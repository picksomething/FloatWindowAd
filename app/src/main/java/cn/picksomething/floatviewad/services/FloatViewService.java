package cn.picksomething.floatviewad.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import cn.picksomething.floatviewad.MyWindowManager;


/**
 * Created by caobin on 15-2-4.
 */
public class FloatViewService extends Service {

    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("caobin", "onStartCommand in FloatViewService");
        if (!MyWindowManager.isFloatAdShow()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyWindowManager.createFloatIconAd(getApplicationContext());
                    MyWindowManager.createFloatFunctionAd(getApplicationContext());
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
