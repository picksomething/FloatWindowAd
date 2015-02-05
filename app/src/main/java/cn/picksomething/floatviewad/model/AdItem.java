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

    public AdItem(int imageId, String title) {
        mImageId = imageId;
        mText = title;
    }

    public AdItem(int imageId, String title, String text) {
        mImageId = imageId;
        mTitle = title;
        mText = text;
    }

    public int getmImageId() {
        return mImageId;
    }

    public String getmTitle() {
        return mTitle;
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
        dest.writeString(this.mText);
    }
}
