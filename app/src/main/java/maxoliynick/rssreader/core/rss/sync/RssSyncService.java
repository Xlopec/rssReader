package maxoliynick.rssreader.core.rss.sync;

import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.inject.Inject;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import maxoliynick.rssreader.R;
import maxoliynick.rssreader.core.rss.api.IApiProvider;
import maxoliynick.rssreader.core.rss.storage.IRssStorage;
import roboguice.service.RoboService;

/**
 *
 */
public final class RssSyncService extends RoboService {

    private static final String TAG = RssSyncService.class.getSimpleName();

    public static final String FILTER = TAG.concat(".filter");
    public static final String ARG_URL = TAG.concat(".url");
    public static final String ARG_SYNC_ITEMS = TAG.concat(".items");
    public static final String ARG_SYNC_EXC = TAG.concat(".exc");

    @Inject
    private IApiProvider apiProvider;
    @Inject
    private IRssStorage storage;

    public RssSyncService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {

            String urlStr = intent.getStringExtra(RssSyncService.ARG_URL);

            try {

                if(urlStr == null) urlStr = getApplicationContext().getString(R.string.default_rss_url);

                apiProvider.fetchRss(new URL(urlStr)).subscribe(result -> {

                    final Intent syncIntent = new Intent(FILTER);

                    syncIntent.putParcelableArrayListExtra(ARG_SYNC_ITEMS, toArrList(result));
                    sendBroadcast(syncIntent);
                    // should be moved off here
                    storage.store(result);
                    stopSelf(startId);
                }, th -> {

                    final Intent syncIntent = new Intent(FILTER);

                    syncIntent.putExtra(ARG_SYNC_ITEMS, th);
                    sendBroadcast(syncIntent);
                    stopSelf(startId);
                });
            } catch (final MalformedURLException e) {
                Log.e(TAG, String.format("Malformed url: %s", urlStr), e);
            }
        } else {
            stopSelf(startId);
        }

        return START_REDELIVER_INTENT;
    }

    @SuppressWarnings("unchecked")
    private static ArrayList<? extends Parcelable> toArrList(@NotNull Collection<? extends Parcelable> collection) {
        return collection instanceof ArrayList ? (ArrayList<? extends Parcelable>) collection : new ArrayList<>(collection);
    }

}
