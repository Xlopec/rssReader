package maxoliynick.rssreader.core.rss.storage.po;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Максим on 11/25/2016.
 */
@DatabaseTable(tableName = "RssImage")
public class ImagePO {

    @DatabaseField(generatedId = true, index = true, columnName = "id", dataType = DataType.LONG)
    private long id;
    @DatabaseField(columnName = "link", dataType = DataType.STRING, canBeNull = false)
    private String link;
    @DatabaseField(columnName = "width", dataType = DataType.INTEGER)
    private int width;
    @DatabaseField(columnName = "height", dataType = DataType.INTEGER)
    private int height;
    @DatabaseField(foreign = true, columnName = "rssItem")
    private RssItemPO parent;

    public ImagePO() {
    }

    public ImagePO(String link, int width, int height, RssItemPO parent) {
        this.link = link;
        this.width = width;
        this.height = height;
        this.parent = parent;
    }

    public RssItemPO getParent() {
        return parent;
    }

    public void setParent(RssItemPO parent) {
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "ImagePO{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", parent=" + parent +
                '}';
    }
}
