package cn.picksomething.floatviewad.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import cn.picksomething.floatviewad.FinishDownloadReceiver;

/**
 * Created by caobin on 15/2/5.
 */
public class DownloadFileUtils {

    private static final String TAG = "DownloadFileUtils";

    private Context mContext;
    private Uri mUri;
    private String mDownloadDir;

    public DownloadFileUtils(Context context, Uri uri, String downloadDir) {
        mContext = context;
        mUri = uri;
        mDownloadDir = downloadDir;
    }

    public void startDownloadAd() {
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(mUri);
        String appName = splitUri(mUri);
        String file = mDownloadDir + "/" + appName;
        Log.d(TAG, "filePath = " + file);
        request.setDestinationUri(Uri.fromFile(new File(file)));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        request.setTitle("FloatAdNotification");
        long id = dm.enqueue(request);
        FinishDownloadReceiver receiver = new FinishDownloadReceiver(id, file);
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private String splitUri(Uri mUri) {
        String[] strArray = mUri.toString().split("/");
        Log.d(TAG, "appName is " + strArray[strArray.length - 1]);
        return strArray[strArray.length - 1];
    }

}
