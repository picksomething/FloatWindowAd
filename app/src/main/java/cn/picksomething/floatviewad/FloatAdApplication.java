package cn.picksomething.floatviewad;

import android.app.Application;
import android.view.WindowManager;

/**
 * Created by caobin on 15-2-5.
 */
public class FloatAdApplication extends Application {
    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }
}
