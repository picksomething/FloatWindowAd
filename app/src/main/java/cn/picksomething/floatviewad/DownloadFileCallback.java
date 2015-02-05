package cn.picksomething.floatviewad;

/**
 * Created by caobin on 15/2/5.
 */
public interface DownloadFileCallback {

    void downloadSuccess(Object obj);

    void downloadError(Exception e, String msg);
}
