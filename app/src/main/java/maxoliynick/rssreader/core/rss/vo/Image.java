package maxoliynick.rssreader.core.rss.vo;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.roboguice.shaded.goole.common.base.Preconditions;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Максим on 11/25/2016.
 */

public final class Image implements Parcelable {

    private final URL link;
    private final int width, height;

    public Image(@NotNull URL link) {
        this(link, -1, -1);
    }

    public Image(@NotNull URL link, int width, int height) {
        this.link = Preconditions.checkNotNull(link);
        this.width = width;
        this.height = height;
    }

    private Image(Parcel in) {
        width = in.readInt();
        height = in.readInt();

        try {
            link = new URL(in.readString());
        } catch (MalformedURLException e) {
            // never will be thrown here
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(link.toExternalForm());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public URL getLink() {
        return link;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (width != image.width) return false;
        if (height != image.height) return false;
        return link.equals(image.link);

    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "link=" + link +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
