package cn.picksomething.floatviewad;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by caobin on 15-2-5.
 */
public class FinishDownloadReceiver extends BroadcastReceiver {

    private static final String TAG = "FinishDownloadReceiver";

    private long mDid;
    private String filePath;

    public FinishDownloadReceiver(long id, String path) {
        mDid = id;
        filePath = path;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            long did = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (did == mDid) {
                File apkFile = new File(filePath);
                if (apkFile.exists()) {
                    Intent newIntent = new Intent(Intent.ACTION_VIEW);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    newIntent.setDataAndType(Uri.fromFile(apkFile),
                            "application/vnd.android.package-archive");
                    context.startActivity(newIntent);
                } else {
                    Log.d(TAG, "install app: file not exists");
                }
                context.unregisterReceiver(this);
            }
        } catch (Exception e) {
        }
    }
}
