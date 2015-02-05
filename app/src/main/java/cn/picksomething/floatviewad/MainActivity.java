package cn.picksomething.floatviewad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.picksomething.floatviewad.services.FloatFunctionAdService;
import cn.picksomething.floatviewad.services.FloatIconAdService;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public Intent intent;
    private Button mStartIconService;
    private Button mStartFunctionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartIconService = (Button) findViewById(R.id.startIconService);
        mStartIconService.setOnClickListener(this);
        mStartFunctionService = (Button) findViewById(R.id.startFuctionService);
        mStartFunctionService.setOnClickListener(this);
        initDatas();
    }

    private void initDatas() {
        intent = new Intent();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startIconService:
                Log.d(TAG, "FloatIconAdService");
                intent.setClass(MainActivity.this, FloatIconAdService.class);
                startService(intent);
                break;
            case R.id.startFuctionService:
                Log.d(TAG, "FloatFunctionAdService");
                intent.setClass(MainActivity.this, FloatFunctionAdService.class);
                startService(intent);
                break;
            default:
                break;
        }
        finish();
    }
}
