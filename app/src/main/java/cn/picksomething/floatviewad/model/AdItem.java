package cn.picksomething.floatviewad.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by caobin on 15-2-5.
 */
public class AdItem implements Parcelable {

    private int mImageId;
    private String mTitle;
    private String mText;
    private String mUri;

    public AdItem(int imageId, String title,String uri) {
        mImageId = imageId;
        mTitle = title;
        mUri =uri;
    }

    public AdItem(int imageId, String title, String uri,String text) {
        mImageId = imageId;
        mTitle = title;
        mUri = uri;
        mText = text;
    }

    public int getmImageId() {
        return mImageId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmUri() {
        return mUri;
    }

    public String getmText() {
        return mText;
    }

    public void setmImageId(int mImageId) {
        this.mImageId = mImageId;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public void setmUri(String mUri) {
        this.mUri = mUri;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mImageId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mUri);
        dest.writeString(this.mText);
    }
}
