package maxoliynick.rssreader.core.mvp.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Px;

import com.google.inject.Inject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import maxoliynick.rssreader.core.mvp.util.IFutureBitmap;
import maxoliynick.rssreader.core.rss.IRssAppService;
import maxoliynick.rssreader.core.rss.bo.RssItem;
import maxoliynick.rssreader.core.rss.vo.Image;
import roboguice.inject.ContextSingleton;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Максим on 11/25/2016.
 */
@ContextSingleton
public final class MainModel implements IMainModel {

    private final IRssAppService appService;

    private static class BitmapHolder implements IFutureBitmap {

        private final String url;
        private Bitmap bitmap;

        public static final Parcelable.Creator<BitmapHolder> CREATOR = new Parcelable.Creator<BitmapHolder>() {
            @Override
            public BitmapHolder createFromParcel(Parcel in) {
                return new BitmapHolder(in);
            }

            @Override
            public BitmapHolder[] newArray(int size) {
                return new BitmapHolder[size];
            }
        };

        BitmapHolder(String url) {
            this.url = url;
        }

        private BitmapHolder(Parcel in) {
            url = in.readString();
        }

        @NotNull
        @Override
        public Observable<Bitmap> fetch(@Px int width, @Px int height, @NotNull Context context) {

            return bitmap == null ? Observable.create(new Observable.OnSubscribe<Bitmap>() {

                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {

                    subscriber.onStart();
                    HttpURLConnection connection = null;

                    try {
                        final URL url = new URL(BitmapHolder.this.url);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();

                        Bitmap bitmap = BitmapFactory.decodeStream(input);
                        BitmapHolder.this.bitmap = bitmap;

                        subscriber.onNext(bitmap);
                    } catch (IOException e) {
                        subscriber.onError(e);
                    } finally {
                        if (connection != null) connection.disconnect();
                        subscriber.onCompleted();
                    }
                }
            }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    : Observable.just(bitmap);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
        }
    }

    @Inject
    public MainModel(IRssAppService appService) {
        this.appService = appService;
    }

    @Override
    public Intent createWebIntent(@NotNull RssItemModel model) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLink().toString()));
    }

    @Override
    public Observable<Collection<RssItemModel>> getCache() {
        return appService.getCache().flatMap(cache -> Observable.just(convert(cache)));
    }

    @Override
    public Observable<Collection<RssItemModel>> fetchRss() {
        return appService.fetchRss().flatMap(result -> Observable.just(convert(result)));
    }

    @Override
    public Observable<Collection<RssItemModel>> fetchRss(@NotNull URL url) {
        return appService.fetchRss(url).flatMap(result -> Observable.just(convert(result)));
    }

    @Override
    public Observable<Collection<RssItemModel>> getOnItemChangedObs() {
        return appService.getOnItemsChangedObs().flatMap(result -> Observable.just(convert(result)));
    }

    private static Collection<RssItemModel> convert(Collection<RssItem> src) {

        final Collection<RssItemModel> result = new ArrayList<>(src.size());

        for (final RssItem item : src) {
            result.add(new RssItemModel(item.getTitle(), item.getDescription(),
                    item.getPubDate().toString("HH:mm:ss"), fromImages(item.getImages()), item.getLink()));
        }

        return result;
    }

    private static IFutureBitmap fromImages(Collection<Image> images) {
        if (images == null || images.isEmpty()) return null;
        return new BitmapHolder(images.iterator().next().getLink().toExternalForm());
    }

}
