package maxoliynick.rssreader.core.rss.api;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Collection;

import maxoliynick.rssreader.core.rss.bo.RssItem;
import rx.Observable;

/**
 * <p>
 * API provider to retrieve data
 * </p>
 * Created by Максим on 11/25/2016.
 */

public interface IApiProvider {

    /**
     * Fetches set of rss items asynchronously from specified url
     *
     * @param url url from which rss items should be retrieved
     * @return {@linkplain Observable} to monitor process state
     */
    Observable<Collection<RssItem>> fetchRss(@NotNull URL url);

}
