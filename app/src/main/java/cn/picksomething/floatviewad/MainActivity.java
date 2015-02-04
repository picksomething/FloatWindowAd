package cn.picksomething.floatviewad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import cn.picksomething.floatviewad.services.FloatViewService;


public class MainActivity extends Activity implements View.OnClickListener {

    public Intent intent;
    private Button mStartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartService = (Button) findViewById(R.id.startService);
        mStartService.setOnClickListener(this);
        initDatas();
    }

    private void initDatas() {
        intent = new Intent(MainActivity.this, FloatViewService.class);
    }

    @Override
    public void onClick(View v) {
        startService(intent);
        Log.d("caobin","startService");
        finish();
    }
}
