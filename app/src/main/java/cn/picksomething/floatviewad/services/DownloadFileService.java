package cn.picksomething.floatviewad.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import cn.picksomething.floatviewad.DownloadFileCallback;
import cn.picksomething.floatviewad.R;
import cn.picksomething.floatviewad.utils.DownloadFileUtils;

/**
 * Created by caobin on 15/2/5.
 */
public class DownloadFileService extends Service {

    private static final String TAG = "DownloadFileService";

    private DownloadFileUtils downloadFileUtils;
    private String filePath;
    private NotificationManager notificationManager;
    private Notification notification;
    private RemoteViews remoteViews;
    private final int notificationID = 1;
    private final int updateProgress = 1;
    private final int downloadSuccess = 2;
    private final int downloadError = 3;
    private Timer timer;
    private TimerTask task;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate in DownloadFileService");
        init();
    }

    private void init() {
        Log.d(TAG, "init in DownloadFileService");
        filePath = Environment.getExternalStorageDirectory() + "/surprise";
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = "Hello";
        remoteViews = new RemoteViews(getPackageName(), R.layout.download_notification);
        remoteViews.setImageViewResource(R.id.IconIV, R.drawable.ic_launcher);
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(updateProgress);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand in DownloadFileService");
        new Thread(new Runnable() {

            @Override
            public void run() {
                downloadFileUtils = new DownloadFileUtils("http://gdown.baidu.com/data/wisegame/b797f4b9634e0833/GOzhuomian_2055.apk", filePath, "GOzhuomian_2055.apk", 3, callback);
                downloadFileUtils.downloadFile();
            }
        }).start();
        timer.schedule(task, 500, 500);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, " onDestroy");
        super.onDestroy();
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == updateProgress) {
                long fileSize = downloadFileUtils.getFileSize();
                long totalReadSize = downloadFileUtils.getTotalReadSize();
                if (totalReadSize > 0) {
                    float size = (float) totalReadSize * 100 / (float) fileSize;
                    DecimalFormat format = new DecimalFormat("0.00");
                    String progress = format.format(size);
                    remoteViews.setTextViewText(R.id.progressTv, "已下载" + progress + "%");
                    remoteViews.setProgressBar(R.id.progressBar, 100, (int) size, false);
                    notification.contentView = remoteViews;
                    notificationManager.notify(notificationID, notification);
                }
            } else if (msg.what == downloadSuccess) {
                remoteViews.setTextViewText(R.id.progressTv, "下载完成");
                remoteViews.setProgressBar(R.id.progressBar, 100, 100, false);
                notification.contentView = remoteViews;
                notificationManager.notify(notificationID, notification);
                if (timer != null && task != null) {
                    timer.cancel();
                    task.cancel();
                    timer = null;
                    task = null;
                }
                stopService(new Intent(getApplicationContext(), DownloadFileService.class));//stop service
            } else if (msg.what == downloadError) {
                if (timer != null && task != null) {
                    timer.cancel();
                    task.cancel();
                    timer = null;
                    task = null;
                }
                notificationManager.cancel(notificationID);
                stopService(new Intent(getApplicationContext(), DownloadFileService.class));//stop service
            }
        }

    };

    DownloadFileCallback callback = new DownloadFileCallback() {

        @Override
        public void downloadSuccess(Object obj) {
            handler.sendEmptyMessage(downloadSuccess);
        }

        @Override
        public void downloadError(Exception e, String msg) {
            handler.sendEmptyMessage(downloadError);
        }
    };
}
