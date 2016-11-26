package maxoliynick.rssreader.core.rss;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Collection;

import maxoliynick.rssreader.core.rss.bo.RssItem;
import rx.Observable;

/**
 * <p>
 * Interface which can be provided to other app services
 * </p>
 * Created by Максим on 11/25/2016.
 */

public interface IRssAppService {

    /**
     * Fetches set of rss items asynchronously from specified url
     *
     * @param url url from which rss items should be retrieved
     * @return {@linkplain Observable} to monitor process state
     */
    Observable<Collection<RssItem>> fetchRss(@NotNull URL url);

    /**
     * Fetches set of rss items asynchronously from default url
     *
     * @return {@linkplain Observable} to monitor process state
     */
    Observable<Collection<RssItem>> fetchRss();

    Observable<Collection<RssItem>> getOnItemsChangedObs();

    @NotNull URL getDefaultUrl();

    /**
     * Fetches set of rss items asynchronously from cache
     *
     * @return {@linkplain Observable} to monitor process state
     */
    Observable<Collection<RssItem>> getCache();

}
