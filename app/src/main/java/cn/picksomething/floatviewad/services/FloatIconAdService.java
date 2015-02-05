package cn.picksomething.floatviewad.services;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import cn.picksomething.floatviewad.R;


/**
 * Created by caobin on 15-2-4.
 */
public class FloatIconAdService extends Service {

    private WindowManager mWindowManager;
    private Handler mHandler;
    private View mFloatIconAd;

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

                mFloatIconAd = createFloatIconAd();
                if (mFloatIconAd.getParent() == null) {
                    mWindowManager.addView(mFloatIconAd, createLayoutParams());
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void initDatas() {
        mHandler = new Handler();
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);


    }

    private View createFloatIconAd() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.float_icon_ad_view, null);
        view.setOnTouchListener(new OnMoveListener());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("caobin", "onClick in createFloatIconAd");
                onFloatIconClick(v);
            }
        });
        return view;
    }

    private void onFloatIconClick(View v) {
        Log.d("caobin", "onFloatIconClick");
        mWindowManager.removeViewImmediate(mFloatIconAd);
        startService(new Intent(getApplicationContext(), DownloadFileService.class));
    }

    private WindowManager.LayoutParams createLayoutParams() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.height = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.float_icon_height);
        lp.width = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.float_icon_width);
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.x = 0;
        lp.y = getWindowHeight() / 2;
        return lp;
    }

    public int getWindowHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public int getWindowWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;

    }

    private class OnMoveListener implements View.OnTouchListener {

        private float mLastPointerX;
        private float mLastPointerY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float currentPointerX, currentPointerY;
            float deltaX, deltaY;
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("caobin", "###ACTION_DOWN###");
                    mLastPointerX = event.getRawX();
                    mLastPointerY = event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.d("caobin", "###ACTION_MOVE###");
                    currentPointerX = event.getRawX();
                    currentPointerY = event.getRawY();

                    deltaX = currentPointerX - mLastPointerX;
                    deltaY = currentPointerY - mLastPointerY;

                    WindowManager.LayoutParams lp = (WindowManager.LayoutParams) mFloatIconAd.getLayoutParams();
                    lp.x = lp.x + (int) deltaX;
                    lp.y = lp.y + (int) deltaY;
                    mWindowManager.updateViewLayout(mFloatIconAd, lp);


                    mLastPointerX = currentPointerX;
                    mLastPointerY = currentPointerY;
                    break;

                case MotionEvent.ACTION_CANCEL:
                    Log.d("caobin", "###ACTION_CANCEL###");
                case MotionEvent.ACTION_UP:
                    Log.d("caobin", "###ACTION_UP###");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        animateToEdge();
                    } else {
                        Log.d("caobin", "###else ###");
                        setX(0);
                    }
                    break;

                default:
                    break;
            }

            return false;
        }

        @SuppressWarnings("NewApi")
        public void animateToEdge() {
            Log.d("caobin", "animateToEdge in ");
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) mFloatIconAd.getLayoutParams();
            int finalX = lp.x < getWindowWidth() / 2 ? 0 : (getWindowWidth() - mFloatIconAd.getWidth());
            ObjectAnimator oa = ObjectAnimator.ofInt(OnMoveListener.this, "X", lp.x, finalX);
            oa.setDuration(200);
            oa.setInterpolator(new AccelerateInterpolator());
            oa.start();


        }

        public void setX(int x) {
            Log.d("caobin", "setX  and x = " + x);
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) mFloatIconAd.getLayoutParams();
            lp.x = x;
            mWindowManager.updateViewLayout(mFloatIconAd, lp);
        }

    }

    private void removeFloatView() {
        if (mFloatIconAd.getParent() != null) {
            mWindowManager.removeViewImmediate(mFloatIconAd);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFloatView();
    }
}
