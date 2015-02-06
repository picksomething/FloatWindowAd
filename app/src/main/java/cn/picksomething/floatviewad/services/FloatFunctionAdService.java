package cn.picksomething.floatviewad.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import cn.picksomething.floatviewad.FloatAdApplication;
import cn.picksomething.floatviewad.R;
import cn.picksomething.floatviewad.adapter.MyAdapter;
import cn.picksomething.floatviewad.model.AdItem;
import cn.picksomething.floatviewad.utils.DownloadFileUtils;
import cn.picksomething.floatviewad.views.FloatFunctionView;

/**
 * Created by caobin on 15-2-5.
 */
public class FloatFunctionAdService extends Service {

    private static final String TAG = "FloatFunctionAdService";

    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mWindowManagerParams = null;
    private FloatFunctionView mFloatFunctionView = null;
    private MyAdapter mMyAdapter = null;
    private Handler mHandler = null;
    LinearLayout linearLayout = null;
    ArrayList<AdItem> adData = null;
    Resources resources = null;
    String[] appName = null;
    String[] appUri = null;
    int[] imagesId = new int[]{R.drawable.qingtingfm,
            R.drawable.wacaibao, R.drawable.wifixinhaozengqiang,
            R.drawable.gozhuomian, R.drawable.yundongke, R.drawable.zhubajie,
            R.drawable.zhiboba, R.drawable.zuoyebang,
            R.drawable.xiaoyuansouti, R.drawable.qqyuedu};

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
        resources = getApplicationContext().getResources();
        appUri = resources.getStringArray(R.array.appUris);
        appName = resources.getStringArray(R.array.appNames);
        mHandler = new Handler();
        adData = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AdItem adItem = new AdItem(imagesId[i], appName[i],
                    appUri[i]);
            adData.add(adItem);
        }
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
        linearLayout = (LinearLayout) inflater.inflate(R.layout.float_ad_list, null);
        ListView listViewAd = (ListView) linearLayout.findViewById(R.id.adList);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeViewImmediate(linearLayout);
            }
        });
        mMyAdapter = new MyAdapter(getApplicationContext(), R.layout.ad_item, adData,linearLayout);
        listViewAd.setAdapter(mMyAdapter);
        /*listViewAd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "You Clicked Item " + position);
                String apkName = adData.get(position).getmTitle();
                Uri uri = Uri.parse(adData.get(position).getmUri());
                Log.d(TAG,"Uri is " + uri);
                String filePath = Environment.getExternalStorageDirectory() + "/surprise";
                DownloadFileUtils downloadFileUtils = new DownloadFileUtils(getApplicationContext(), uri, filePath,apkName);
                downloadFileUtils.startDownloadAd();
                mWindowManager.removeViewImmediate(linearLayout);
            }
        });*/
        mWindowManager.addView(linearLayout, createAdListParams());
    }

    private WindowManager.LayoutParams createAdListParams() {
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        mWindowManagerParams = ((FloatAdApplication) getApplication()).getWindowParams();
        mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
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
