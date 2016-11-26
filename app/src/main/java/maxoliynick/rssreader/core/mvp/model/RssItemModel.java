package maxoliynick.rssreader.core.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;

import maxoliynick.rssreader.core.mvp.util.IFutureBitmap;

/**
 * Created by Максим on 11/25/2016.
 */
public final class RssItemModel implements Parcelable {

    private final String title;
    private final String description;
    private final String timestamp;
    private final IFutureBitmap bitmap;
    private final URL link;

    RssItemModel(String title, String description, String timestamp, IFutureBitmap bitmap, URL link) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.bitmap = bitmap;
        this.link = link;
    }

    private RssItemModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        timestamp = in.readString();
        try {
            link = new URL(in.readString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        bitmap = in.readParcelable(IFutureBitmap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(timestamp);
        dest.writeString(link.toExternalForm());
        dest.writeParcelable(bitmap, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RssItemModel> CREATOR = new Creator<RssItemModel>() {
        @Override
        public RssItemModel createFromParcel(Parcel in) {
            return new RssItemModel(in);
        }

        @Override
        public RssItemModel[] newArray(int size) {
            return new RssItemModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public IFutureBitmap getBitmap() {
        return bitmap;
    }

    public URL getLink() {
        return link;
    }
}
