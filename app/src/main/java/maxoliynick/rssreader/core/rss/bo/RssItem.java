package maxoliynick.rssreader.core.rss.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.roboguice.shaded.goole.common.base.Preconditions;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import maxoliynick.rssreader.core.rss.vo.Image;
import maxoliynick.rssreader.core.rss.vo.RssID;

/**
 * <p>
 *     Aggregate root which describes rss item
 * </p>
 * Created by Максим on 11/25/2016.
 */

public final class RssItem implements Parcelable {

    private final RssID id;
    private final String title;
    private final String description;
    private final URL link;
    private final List<Image> images;
    private final DateTime pubDate;

    /**
     * Builder to create a new instance of {@linkplain RssItem}
     */
    public static final class Builder {

        private final RssID id;
        private final URL link;
        private final String title;
        private final String description;
        private final List<Image> images;
        private final DateTime pubDate;

        public Builder(@NotNull String title, @NotNull String description, @NotNull RssID id,
                       @NotNull URL link, @NotNull DateTime pubDate) {
            this.title = Preconditions.checkNotNull(title);
            this.description = Preconditions.checkNotNull(description);
            this.id = Preconditions.checkNotNull(id);
            this.link = Preconditions.checkNotNull(link);
            this.images = new ArrayList<>(0);
            this.pubDate = Preconditions.checkNotNull(pubDate);
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public List<Image> getImages() {
            return Collections.unmodifiableList(images);
        }

        public Builder setImages(Collection<Image> images) {
            this.images.clear();
            if (images != null) {
                this.images.addAll(images);
            }
            return this;
        }

        public DateTime getPubDate() {
            return pubDate;
        }

        public URL getLink() {
            return link;
        }

        public Builder addImage(@NotNull Image image) {
            images.add(image);
            return this;
        }

        public Builder removeImage(@NotNull Image image) {
            images.remove(image);
            return this;
        }

        public RssID getId() {
            return id;
        }

        public RssItem build() {
            return new RssItem(this);
        }
    }

    private RssItem(@NotNull Builder builder) {
        Preconditions.checkNotNull(builder);
        this.id = Preconditions.checkNotNull(builder.getId());
        this.title = Preconditions.checkNotNull(builder.getTitle());
        this.description = Preconditions.checkNotNull(builder.getDescription());
        this.images = Collections.unmodifiableList(builder.getImages());
        this.link = Preconditions.checkNotNull(builder.getLink());
        this.pubDate = Preconditions.checkNotNull(builder.getPubDate());
    }

    protected RssItem(Parcel in) {
        id = in.readParcelable(RssID.class.getClassLoader());
        title = in.readString();
        description = in.readString();

        final ArrayList<Image> tmp = new ArrayList<>();
        in.readTypedList(tmp, Image.CREATOR);
        tmp.trimToSize();
        images = Collections.unmodifiableList(tmp);
        link = (URL) in.readSerializable();
        pubDate = (DateTime) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeTypedList(images);
        dest.writeSerializable(link);
        dest.writeSerializable(pubDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel in) {
            return new RssItem(in);
        }

        @Override
        public RssItem[] newArray(int size) {
            return new RssItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Collection<Image> getImages() {
        return images;
    }

    public RssID getId() {
        return id;
    }

    public URL getLink() {
        return link;
    }

    public DateTime getPubDate() {
        return pubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RssItem item = (RssItem) o;

        if (!id.equals(item.id)) return false;
        if (!title.equals(item.title)) return false;
        if (!description.equals(item.description)) return false;
        if (!link.equals(item.link)) return false;
        if (!images.equals(item.images)) return false;
        return pubDate.equals(item.pubDate);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + link.hashCode();
        result = 31 * result + images.hashCode();
        result = 31 * result + pubDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RssItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link=" + link +
                ", images=" + images +
                ", pubDate=" + pubDate +
                '}';
    }
}
