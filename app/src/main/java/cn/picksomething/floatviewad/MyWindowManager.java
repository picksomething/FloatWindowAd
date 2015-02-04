package cn.picksomething.floatviewad;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;

import cn.picksomething.floatviewad.views.FloatFunctionAdView;
import cn.picksomething.floatviewad.views.FloatIconAdView;

/**
 * Created by caobin on 15-2-4.
 */
public class MyWindowManager {

    private static FloatIconAdView floatIconAdView;
    private static FloatFunctionAdView floatFunctionAdView;

    private static LayoutParams floatIconAdViewParams;
    private static LayoutParams floatFunctionAdViewParams;

    private static WindowManager mWindowManager;

    /**
     * create float icon ad
     *
     * @param context must be Context of Application
     */
    public static void createFloatIconAd(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (floatIconAdView == null) {
            floatIconAdView = new FloatIconAdView(context);
            if (floatIconAdViewParams == null) {
                floatIconAdViewParams = new LayoutParams();
                floatIconAdViewParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                floatIconAdViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                floatIconAdViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                floatIconAdViewParams.width = floatIconAdView.viewWidth;
                floatIconAdViewParams.height = floatIconAdView.viewHeight;
                floatIconAdViewParams.x = 0;
                floatIconAdViewParams.y = getWindowHeight() / 2;
            }
            floatIconAdView.setParams(floatIconAdViewParams);
            windowManager.addView(floatIconAdView, floatIconAdViewParams);
        }
    }

    /**
     * remove float icon ad
     *
     * @param context
     */
    public static void removeFloatIconAd(Context context) {
        if (floatIconAdView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(floatIconAdView);
            floatIconAdView = null;
        }
    }

    /**
     * create float function ad
     *
     * @param context
     */
    public static void createFloatFunctionAd(Context context) {
        WindowManager windowManager = getWindowManager(context);
        if (floatFunctionAdView == null) {
            floatFunctionAdView = new FloatFunctionAdView(context);
            if (floatFunctionAdViewParams == null) {
                floatFunctionAdViewParams = new LayoutParams();
                floatFunctionAdViewParams.x = 0;
                floatFunctionAdViewParams.y = getWindowHeight() / 2;
                floatFunctionAdViewParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
                floatFunctionAdViewParams.gravity = Gravity.RIGHT | Gravity.TOP;
                floatFunctionAdViewParams.width = floatFunctionAdView.viewWidth;
                floatFunctionAdViewParams.height = floatFunctionAdView.viewHeight;
            }
            windowManager.addView(floatFunctionAdView, floatFunctionAdViewParams);
        }
    }

    /**
     * remove float function ad
     *
     * @param context
     */
    public static void removeFloatFunctionAd(Context context) {
        if (floatFunctionAdView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(floatFunctionAdView);
            floatFunctionAdView = null;
        }
    }

    /**
     * whether float ad show
     *
     * @return
     */
    public static boolean isFloatAdShow() {
        return floatIconAdView != null || floatFunctionAdView != null;
    }

    /**
     * If WindowManager isn't created create it,or return current WindowManager
     *
     * @param context
     * @return instance of WindowManager, to control float ad
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public static void createDownloadList() {

    }

    public static int getWindowWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getWindowHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    @SuppressWarnings("NewApi")
    public static void animateToEdge(View.OnTouchListener onTouchListener, View view){
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams)view.getLayoutParams();
        int finalX = lp.x < getWindowWidth() / 2 ? 0 : (getWindowWidth() - view.getWidth());
        ObjectAnimator oa = ObjectAnimator.ofInt(onTouchListener,"X",lp.x,finalX);
        oa.setDuration(200);
        oa.setInterpolator(new AccelerateInterpolator());
        oa.start();
    }


}
