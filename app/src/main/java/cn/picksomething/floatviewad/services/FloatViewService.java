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
public class FloatViewService extends Service {

    private static final int ICON_AD = 0;
    private static final int MULTI_KIND_AD = 1;
    private int currentKindAd = ICON_AD;

    private WindowManager mWindowManager;
    private Handler mHandler;
    private View mFloatIconAd;
    private View mFloatFunctionAd;
    private View mFloatAdList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("caobin", "onStartCommand in FloatViewService");
        initDatas();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (currentKindAd) {
                    case ICON_AD:
                        mFloatIconAd = createFloatIconAd();
                        if (mFloatIconAd.getParent() == null) {
                            mWindowManager.addView(mFloatIconAd, createLayoutParams());
                        }
                        break;
                    case MULTI_KIND_AD:
                        mFloatFunctionAd = createFloatFunctionAd();
                        if (mFloatFunctionAd.getParent() == null) {
                            mWindowManager.addView(mFloatFunctionAd, createLayoutParams());
                        }
                        break;
                    default:
                        break;
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

    private View createFloatFunctionAd() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.float_function_ad_view, null);
        view.setOnTouchListener(new OnMoveListener());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("caobin", "onClick in createFloatIconAd");
                onFloatFunctionClick(v);
            }
        });
        return view;
    }

    private void onFloatFunctionClick(View v) {
        mFloatAdList = createFloatAdList();
        if (mFloatAdList.getParent() == null) {
            mWindowManager.addView(mFloatAdList, createAdListParams());
        }
    }

    private View createFloatAdList() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View view = inflater.inflate(R.layout.float_ad_list, null);
        return view;
    }

    private WindowManager.LayoutParams createAdListParams() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.height = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.float_list_height);
        lp.width = getApplicationContext().getResources().getDimensionPixelSize(R.dimen.float_list_width);
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        lp.x = getWindowWidth() / 2;
        lp.y = getWindowHeight() / 2;
        return lp;
    }

    private void onFloatIconClick(View v) {
        Log.d("caobin", "onFloatIconClick in FloatViewService");
        startService(new Intent(getApplicationContext(), DownloadFileService.class));
        mWindowManager.removeView(mFloatIconAd);
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
                    mLastPointerX = event.getRawX();
                    mLastPointerY = event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
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
                case MotionEvent.ACTION_UP:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        animateToEdge();
                    } else {
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
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) mFloatIconAd.getLayoutParams();
            int finalX = lp.x < getWindowWidth() / 2 ? 0 : (getWindowWidth() - mFloatIconAd.getWidth());
            ObjectAnimator oa = ObjectAnimator.ofInt(OnMoveListener.this, "X", lp.x, finalX);
            oa.setDuration(200);
            oa.setInterpolator(new AccelerateInterpolator());
            oa.start();
        }

        public void setX(int x) {
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) mFloatIconAd.getLayoutParams();
            lp.x = x;
            mWindowManager.updateViewLayout(mFloatIconAd, lp);
        }

    }

    private void removeFloatView() {
        if (mFloatIconAd.getParent() != null) {
            // If Parent is not null, it shows that the view is on the screen
            mWindowManager.removeViewImmediate(mFloatIconAd);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFloatView();
    }
}
