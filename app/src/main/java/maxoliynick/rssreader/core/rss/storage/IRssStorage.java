package maxoliynick.rssreader.core.rss.storage;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import maxoliynick.rssreader.core.rss.bo.RssItem;

/**
 * <p>
 * Represents storage contract
 * </p>
 * Created by Максим on 11/25/2016.
 */

public interface IRssStorage {

    /**
     * Returns all rss items from storage implementation
     */
    @NotNull
    Collection<RssItem> getAllItems();

    /**
     * Stores collection of rss items
     *
     * @param items items to store
     */
    void store(@NotNull Collection<RssItem> items);

}
