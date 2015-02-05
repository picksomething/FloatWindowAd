package cn.picksomething.floatviewad.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.picksomething.floatviewad.FloatAdApplication;
import cn.picksomething.floatviewad.R;

/**
 * Created by caobin on 15-2-5.
 */
public class FloatIconView extends ImageView {

    private static final String TAG = "FloatIconView";

    private float mTouchX;
    private float mTouchY;
    private float x;
    private float y;
    private boolean isMove;
    private OnClickListener mClickListener;
    private int mDefaultImage = R.drawable.ic_new;
    private Context mContext;

    private WindowManager mWindowManager = (WindowManager) getContext()
            .getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    private WindowManager.LayoutParams mLayoutParams = ((FloatAdApplication) getContext()
            .getApplicationContext()).getWindowParams();

    public FloatIconView(Context context) {
        super(context);
        mContext = context;
        isMove = false;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mLayoutParams.height = mContext.getResources().getDimensionPixelSize(R.dimen.float_icon_height);
        mLayoutParams.width = mContext.getResources().getDimensionPixelSize(R.dimen.float_icon_width);
        mLayoutParams.x = 0;
        mLayoutParams.y = getWindowHeight() / 2;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Log.d(TAG, "statusBarHeight:" + statusBarHeight);
        x = event.getRawX();
        y = event.getRawY() - statusBarHeight;
        Log.d(TAG, "====currX " + x + "====currY " + y);

        int screenWidth = getWindowWidth();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                isMove = false;
                Log.d(TAG, "====startX " + mTouchX + "====startY " + mTouchY);
                break;
            case MotionEvent.ACTION_MOVE:
                if (x > 35 && (screenWidth - x) > 35) {
                    isMove = true;
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    isMove = false;
                    float halfScreen = screenWidth / 2;
                    if (x <= halfScreen) {
                        x = 0;
                    } else {
                        x = screenWidth;
                    }
                    updateViewPosition();
                } else {
                    if (mClickListener != null) {
                        mClickListener.onClick(this);
                    }
                }
                mTouchX = mTouchY = 0;
                break;
        }
        return true;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        this.mClickListener = listener;
    }

    private void updateViewPosition() {
        mLayoutParams.x = (int) (x - mTouchX);
        mLayoutParams.y = (int) (y - mTouchY);
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }
}
