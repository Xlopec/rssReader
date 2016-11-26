package maxoliynick.rssreader.core.mvp.model;

import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Collection;

import rx.Observable;

/**
 * Created by Максим on 11/25/2016.
 */

public interface IMainModel {

    Intent createWebIntent(@NotNull RssItemModel model);

    Observable<Collection<RssItemModel>> getCache();

    Observable<Collection<RssItemModel>> fetchRss();

    Observable<Collection<RssItemModel>> fetchRss(@NotNull URL url);

    Observable<Collection<RssItemModel>> getOnItemChangedObs();
}
