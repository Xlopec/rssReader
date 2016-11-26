package maxoliynick.rssreader.core.rss.storage;

import android.content.Context;
import android.util.Log;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.ReferenceObjectCache;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

import maxoliynick.rssreader.core.rss.bo.RssItem;
import maxoliynick.rssreader.core.rss.storage.po.RssItemPO;

/**
 * Created by Максим on 11/25/2016.
 */
@Singleton
public final class RssStorage implements IRssStorage {

    private static final String TAG = RssStorage.class.getSimpleName();

    private final DatabaseHelper helper;

    @Inject
    public RssStorage(Context context, DatabaseHelper helper) {
        this.helper = helper;
    }

    @NotNull
    @Override
    public Collection<RssItem> getAllItems() {

        try {
            return Collections.unmodifiableCollection(Mapper.fromPo(createDao().queryForAll()));
        } catch (final SQLException e) {
            Log.e(TAG, "exception while querying category table", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void store(@NotNull Collection<RssItem> items) {

        try {
            final BaseDaoImpl<RssItemPO, Long> dao = createDao();
            helper.clear(RssItemPO.class);
            dao.create(Mapper.toPo(items, dao));
        } catch (final Exception e) {
            Log.e(TAG, "exception while updating table", e);
            throw new RuntimeException(e);
        }
    }

    private BaseDaoImpl<RssItemPO, Long> createDao() {

        try {
            final BaseDaoImpl<RssItemPO, Long> dao = helper.getDao(RssItemPO.class);
            dao.setObjectCache(true);
            dao.setObjectCache(ReferenceObjectCache.makeSoftCache());
            return dao;
        } catch (final SQLException e) {
            Log.e(TAG, "Exception while creating dao", e);
            helper.close();
            throw new RuntimeException(e);
        }
    }
}
