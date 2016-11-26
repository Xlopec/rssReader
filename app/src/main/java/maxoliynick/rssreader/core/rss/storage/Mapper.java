package maxoliynick.rssreader.core.rss.storage;

import android.util.Log;

import com.j256.ormlite.dao.BaseDaoImpl;

import org.jetbrains.annotations.NotNull;
import org.roboguice.shaded.goole.common.base.Preconditions;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import maxoliynick.rssreader.core.rss.bo.RssItem;
import maxoliynick.rssreader.core.rss.storage.po.ImagePO;
import maxoliynick.rssreader.core.rss.storage.po.RssItemPO;
import maxoliynick.rssreader.core.rss.vo.Image;
import maxoliynick.rssreader.core.rss.vo.RssID;

/**
 * Created by Максим on 11/25/2016.
 */
final class Mapper {

    private static final String TAG = Mapper.class.getSimpleName();

    private Mapper() {
        throw new RuntimeException("illegal call");
    }

    static Collection<RssItem> fromPo(@NotNull Collection<RssItemPO> src) {
        Preconditions.checkNotNull(src);
        final Collection<RssItem> result = new ArrayList<>(src.size());

        for (final RssItemPO po : src) {

            try {
                result.add(new RssItem.Builder(po.getTitle(), po.getDescription(), new RssID(UUID.fromString(po.getId())),
                        new URL(po.getLink()), po.getDate())
                        .setImages(fromImagePo(po.getImages()))
                        .build()
                );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    static Collection<RssItemPO> toPo(@NotNull Collection<RssItem> src, @NotNull BaseDaoImpl<RssItemPO, Long> dao) {
        Preconditions.checkNotNull(src);
        final Collection<RssItemPO> result = new ArrayList<>(src.size());

        for (final RssItem item : src) {
            result.add(toPo(item, dao));
        }
        return result;
    }

    static RssItemPO toPo(@NotNull RssItem src, BaseDaoImpl<RssItemPO, Long> dao) {
        Preconditions.checkNotNull(src);

        final RssItemPO po = new RssItemPO(src.getId().getUuid().toString(), src.getTitle(),
                src.getDescription(), src.getLink().toExternalForm(), src.getPubDate());

        try {
            dao.assignEmptyForeignCollection(po, RssItemPO.IMAGES_FIELD);
            po.addAll(toPo(po, src.getImages()));
        } catch (final SQLException e) {
            return null;
        }

        return po;
    }

    private static Collection<ImagePO> toPo(RssItemPO parent, Collection<Image> images) {

        final Collection<ImagePO> result = new ArrayList<>(images.size());

        for (final Image image : images) {
            result.add(new ImagePO(image.getLink().toExternalForm(), image.getWidth(), image.getHeight(), parent));
        }

        return result;
    }

    private static Collection<Image> fromImagePo(Collection<ImagePO> images) {

        final Collection<Image> result = new ArrayList<>(images.size());

        for (final ImagePO image : images) {
            try {
                result.add(new Image(new URL(image.getLink()), image.getWidth(), image.getHeight()));
            } catch (MalformedURLException e) {
                Log.e(TAG, "failed to parse url", e);
            }
        }

        return result;
    }

}
