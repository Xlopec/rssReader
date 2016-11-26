package maxoliynick.rssreader.core.rss.vo;

import android.os.Parcel;
import android.os.Parcelable;

import org.roboguice.shaded.goole.common.base.Preconditions;

import java.util.UUID;

/**
 * <p>
 *     Value object, which represents rss
 * </p>
 * Created by Максим on 11/25/2016.
 */

public final class RssID implements Parcelable {

    private final UUID uuid;

    public RssID(UUID uuid) {
        this.uuid = Preconditions.checkNotNull(uuid);
    }

    private RssID(Parcel in) {
        uuid = UUID.fromString(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RssID> CREATOR = new Creator<RssID>() {
        @Override
        public RssID createFromParcel(Parcel in) {
            return new RssID(in);
        }

        @Override
        public RssID[] newArray(int size) {
            return new RssID[size];
        }
    };

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssID rssID = (RssID) o;

        return uuid.equals(rssID.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return "RssID{" +
                "uuid=" + uuid +
                '}';
    }
}
