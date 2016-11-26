package maxoliynick.rssreader.core.rss.storage.po;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Created by Максим on 11/25/2016.
 */
@DatabaseTable(tableName = "RssItem")
public class RssItemPO {

    public static final String ID_FIELD = "id";
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String LINK_FIELD = "link";
    public static final String IMAGES_FIELD = "images";
    public static final String DATE_FIELD = "pub";

    @DatabaseField(id = true, index = true, columnName = ID_FIELD, dataType = DataType.STRING)
    private String id;
    @DatabaseField(columnName = LINK_FIELD, dataType = DataType.STRING, canBeNull = false)
    private String link;
    @DatabaseField(columnName = TITLE_FIELD, dataType = DataType.STRING, canBeNull = false)
    private String title;
    @DatabaseField(columnName = DESCRIPTION_FIELD, dataType = DataType.STRING, canBeNull = false)
    private String description;
    @DatabaseField(columnName = DATE_FIELD, dataType = DataType.DATE_TIME, canBeNull = false)
    private DateTime date;
    @ForeignCollectionField(eager = true, columnName = IMAGES_FIELD)
    private ForeignCollection<ImagePO> images;

    public RssItemPO() {
    }

    public RssItemPO(String id, String title, String description, String link, DateTime date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ForeignCollection<ImagePO> getImages() {
        return images;
    }

    public boolean add(ImagePO obj) {
        return images.add(obj);
    }

    public boolean addAll(Collection<? extends ImagePO> c) {
        return images.addAll(c);
    }

    @Override
    public String toString() {
        return "RssItemPO{" +
                "id='" + id + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", images=" + images +
                '}';
    }
}
