package cn.picksomething.floatviewad.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.picksomething.floatviewad.DownloadFileCallback;

/**
 * Created by caobin on 15/2/5.
 */
public class DownloadFileUtils {
    private final String TAG = "DownloadFileUtils";
    private String url;
    private long fileSize;
    private long totalReadSize;
    private long block;
    private int threadCount;
    private final int threadPoolNum = 5;
    private final int bufferSize = 1024 * 100;
    private String fileName;
    private String filePath;
    private HttpURLConnection urlConnection;
    private RandomAccessFile randomAccessFile;
    private URL uri;
    private DownloadFileCallback callback;
    private ExecutorService executorService;
    private volatile boolean error = false;
    private File[] tempFiles;
    public DownloadFileUtils(String url,String filePath,String fileName,int threadCount,DownloadFileCallback callback){
        this.url = url;
        this.filePath = filePath;
        this.fileName = fileName;
        this.threadCount = threadCount;
        this.callback = callback;
        tempFiles = new File[threadCount];
    }

    public long getFileSize() {
        return fileSize;
    }
    public long getTotalReadSize() {
        return totalReadSize;
    }

    public boolean downloadFile(){
        try {
            uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            urlConnection.setRequestMethod("GET");
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                fileSize = urlConnection.getContentLength();
                block = fileSize / threadCount + 1;
                File file = new File(filePath,fileName);
                if(!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                executorService = Executors.newFixedThreadPool(threadPoolNum);
                CountDownLatch countDownLatch = new CountDownLatch(threadCount);
                for(int i = 0; i < threadCount; i++){
                    long startPosition = i * block;
                    long endPosition = (i+1) * block - 1;
                    randomAccessFile = new RandomAccessFile(file, "rwd");
                    executorService.execute(new DownloadThread(i+1, startPosition, endPosition, randomAccessFile,countDownLatch));
                }
                countDownLatch.await();
                for(int i = 0; i < threadCount; i++){
                    if(tempFiles[i] != null && tempFiles[i].exists())
                        tempFiles[i].delete();
                }
                executorService.shutdown();
                callback.downloadSuccess(null);
                Log.i(TAG, "DownloadFile");
                return true;
            }
        } catch (Exception e) {
            callback.downloadError(e, "");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    class DownloadThread implements Runnable{
        private int threadId;
        private long startPosition;
        private long endPosition;
        private RandomAccessFile randomAccessFile;
        private CountDownLatch countDownLatch;
        private boolean isFirst = true;
        private long[] startPositions;
        private long[] endPositions;
        private File tempFile;
        public DownloadThread(int threadId,long startPosition,long endPosition,RandomAccessFile randomAccessFile,CountDownLatch countDownLatch){
            this.threadId = threadId;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.randomAccessFile = randomAccessFile;
            this.countDownLatch = countDownLatch;
            tempFile = new File(filePath+"/thread"+threadId,fileName.replaceAll(".apk", ".position"));
            tempFiles[threadId - 1] = tempFile;
            if(tempFile.exists()){
                isFirst = false;
                readPositionInfo();
            }else{
                tempFile.getParentFile().mkdirs();
                startPositions = new long[threadCount];
                endPositions = new long[threadCount];
            }
        }
        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setConnectTimeout(5 * 60 * 1000);
                connection.setReadTimeout(60 * 1000);
                connection.setAllowUserInteraction(true);
                if (isFirst) {
                    randomAccessFile.seek(startPosition);
                }else{
                    startPosition = startPositions[threadId - 1];
                    endPosition = endPositions[threadId - 1];
                    randomAccessFile.seek(startPosition);
                }
                connection.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);// ÉèÖÃÃ¿ÌõÏß³Ì¿ªÊ¼ÏÂÔØµÄÎ»ÖÃ
                InputStream inputStream = new BufferedInputStream(connection.getInputStream(), bufferSize);// Ê¹ÓÃ»º³åÇø¶ÁÈ¡ÎÄ¼þ
                byte[] b = new byte[bufferSize];
                int len = 0;
                long readSize = startPosition;
                while (!error && (len = inputStream.read(b)) != -1) {
                    randomAccessFile.write(b, 0, len);
                    totalReadSize += len;
                    readSize += len;
                    savePositionInfo(readSize, endPosition,totalReadSize);
                }
                if (!error)
                    Log.d(TAG, "thread " + threadId + "error");
                else
                    Log.e(TAG, "thread " + threadId + "error");
                inputStream.close();
                randomAccessFile.close();
                connection.disconnect();
                countDownLatch.countDown();
            } catch (Exception e) {
                Log.e(TAG, "thread" + threadId + "error");
                error = true;
                e.printStackTrace();
                callback.downloadError(e, "");
            }
        }

        private void savePositionInfo(long startPosition,long endPosition,long totalReadSize){
            try {
                DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(tempFile));
                outputStream.writeInt(startPositions.length);
                outputStream.writeLong(totalReadSize);
                for(int i = 0; i < startPositions.length; i++){
                    outputStream.writeLong(startPosition);
                    outputStream.writeLong(endPosition);
                }
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * ¶ÁÈ¡ÁÙÊ±ÎÄ¼þ
         */
        private void readPositionInfo(){
            try {
                DataInputStream inputStream = new DataInputStream(new FileInputStream(tempFile));
                int startPositionLength = inputStream.readInt();
                totalReadSize = inputStream.readLong();
                startPositions = new long[startPositionLength];
                endPositions = new long[startPositionLength];
                for(int i = 0; i < startPositionLength; i++){
                    startPositions[i] = inputStream.readLong();
                    endPositions[i] = inputStream.readLong();
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
