package maxoliynick.rssreader.core.rss;

import com.google.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Collection;

import javax.inject.Inject;

import maxoliynick.rssreader.core.rss.bo.RssItem;
import maxoliynick.rssreader.core.rss.storage.IRssStorage;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 11/25/2016.
 */
@Singleton
public final class RssAppService implements IRssAppService {

    private final IRssDomain domain;
    private final IRssStorage storage;

    @Inject
    public RssAppService(IRssDomain domain, IRssStorage storage) {
        this.domain = domain;
        this.storage = storage;
    }

    @Override
    public Observable<Collection<RssItem>> fetchRss(@NotNull URL url) {
        return domain.fetchRss(url).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Collection<RssItem>> fetchRss() {
        return domain.fetchRss().observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Collection<RssItem>> getOnItemsChangedObs() {
        return domain.getOnItemsChangedObs();
    }

    @NotNull
    @Override
    public URL getDefaultUrl() {
        return domain.getDefaultUrl();
    }

    @Override
    public Observable<Collection<RssItem>> getCache() {
        return Observable.just(storage.getAllItems()).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread());
    }
}
