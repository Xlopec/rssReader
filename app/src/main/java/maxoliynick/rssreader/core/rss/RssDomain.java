package maxoliynick.rssreader.core.rss;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.inject.Singleton;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collection;

import javax.inject.Inject;

import maxoliynick.rssreader.R;
import maxoliynick.rssreader.core.rss.api.IApiProvider;
import maxoliynick.rssreader.core.rss.bo.RssItem;
import maxoliynick.rssreader.core.rss.storage.IRssStorage;
import maxoliynick.rssreader.core.rss.sync.RssSyncService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Максим on 11/25/2016.
 */
@Singleton
public final class RssDomain implements IRssDomain {

    private final URL defaultUrl;
    private final IApiProvider apiProvider;
    private final IRssStorage storage;
    private final PublishSubject<Collection<RssItem>> itemsChangedObs;

    @Inject
    public RssDomain(Context context, IApiProvider apiProvider, IRssStorage storage, AlarmManager alarmManager) {

        this.apiProvider = apiProvider;
        this.storage = storage;

        try {
            this.defaultUrl = new URL(context.getString(R.string.default_rss_url));
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }

        itemsChangedObs = PublishSubject.create();

        final Intent intent = new Intent(context, RssSyncService.class);

        intent.putExtra(RssSyncService.ARG_URL, defaultUrl.toExternalForm());

        final PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);
        final Calendar calendar = Calendar.getInstance();

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        context.getApplicationContext().registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                final Collection<RssItem> rssItems = intent.getParcelableArrayListExtra(RssSyncService.ARG_SYNC_ITEMS);

                if(rssItems != null) {
                    itemsChangedObs.onNext(rssItems);
                }
            }
        }, new IntentFilter(RssSyncService.FILTER));
    }

    @Override
    public Observable<Collection<RssItem>> fetchRss(@NotNull URL url) {
        return apiProvider.fetchRss(url).flatMap(items -> {
            storage.store(items);
            return Observable.just(items);
        });
    }

    @Override
    public Observable<Collection<RssItem>> fetchRss() {
        return fetchRss(defaultUrl);
    }

    @NotNull
    @Override
    public URL getDefaultUrl() {
        return defaultUrl;
    }

    @Override
    public Observable<Collection<RssItem>> getOnItemsChangedObs() {
        return itemsChangedObs.asObservable().observeOn(AndroidSchedulers.mainThread());
    }
}
