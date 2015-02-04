package cn.picksomething.floatviewad.views;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cn.picksomething.floatviewad.MyWindowManager;
import cn.picksomething.floatviewad.R;

/**
 * Created by caobin on 15-2-4.
 */
public class FloatIconAdView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    public FloatIconAdView(Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_icon_ad_view, this);
        View view = findViewById(R.id.small_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        view.setOnTouchListener(new OnMoveListener());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFloatIconClick(v);
            }
        });
    }

    private void onFloatIconClick(View v) {

    }

    public class OnMoveListener implements View.OnTouchListener{

        private float mLastPointerX;
        private float mLastPointerY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float currentPointerX, currentPointerY;
            float deltaX, deltaY;
            int action = event.getAction();
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    mLastPointerX = event.getRawX();
                    mLastPointerY = event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    currentPointerX = event.getRawX();
                    currentPointerY = event.getRawY();

                    deltaX = currentPointerX - mLastPointerX;
                    deltaY = currentPointerY - mLastPointerY;

                    WindowManager.LayoutParams lp = (WindowManager.LayoutParams)FloatIconAdView.this.getLayoutParams();
                    lp.x = lp.x  + (int)deltaX;
                    lp.y = lp.y  + (int)deltaY;
                    mWindowManager.updateViewLayout(FloatIconAdView.this, lp);

                    mLastPointerX = currentPointerX;
                    mLastPointerY = currentPointerY;
                    break;

                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
                        MyWindowManager.animateToEdge(this, FloatIconAdView.this);
                    }else{
                        setX(0);
                    }
                    break;

                default:break;
            }

            return false;
        }
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }


    private void startDownload() {
        MyWindowManager.createFloatFunctionAd(getContext());
        MyWindowManager.removeFloatIconAd(getContext());
    }
}
