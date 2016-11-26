package maxoliynick.rssreader.core.mvp.presenter;

import android.os.Bundle;
import android.util.Log;

import com.google.inject.Inject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.roboguice.shaded.goole.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;

import maxoliynick.rssreader.core.mvp.model.IMainModel;
import maxoliynick.rssreader.core.mvp.model.RssItemModel;
import maxoliynick.rssreader.core.mvp.view.IMainView;
import roboguice.inject.ContextSingleton;
import rx.Subscription;

/**
 * Created by Максим on 11/25/2016.
 */
@ContextSingleton
public final class MainPresenter extends IMainPresenter {

    private final static String TAG = MainPresenter.class.getSimpleName();
    private static final String ARG_CACHE = "argCache";

    private final IMainModel model;
    private final ArrayList<RssItemModel> localCache;
    private final Subscription itemChangedObsSubscr;

    @Inject
    public MainPresenter(IMainModel model) {

        this.model = model;
        this.localCache = new ArrayList<>();
        this.itemChangedObsSubscr = model.getOnItemChangedObs().subscribe(items -> {
            if(!isViewGone()) {
                syncWithView(items);
            }
        });
    }

    @Override
    public void onItemClick(@NotNull RssItemModel itemModel) {
        getView().startActivity(model.createWebIntent(itemModel));
    }

    @Override
    public void onRefresh() {

        model.fetchRss()
                .doOnSubscribe(getView()::showRefreshProgress)
                .doOnCompleted(getView()::hideRefreshProgress)
                .subscribe(result -> {
                    if (!isViewGone()) {
                        syncWithView(result);
                        getView().hideRefreshProgress();
                    }
                }, th -> {
                    if (!isViewGone()) {
                        getView().hideRefreshProgress();
                    }
                    Log.w(TAG, "error occurred on refresh#", th);
                }
        );
    }

    @Override
    protected void onViewAttached(@NotNull IMainView view, @Nullable Bundle savedState, @Nullable Bundle data) {

        if (isFirstTimeAttached()) {
            setupView(savedState);
        }
    }

    private void setupView(Bundle savedState) {

        model.getCache().subscribe(cache -> {

            if (savedState == null) {

                if (cache.isEmpty()) {
                    model.fetchRss()
                            .doOnSubscribe(getView()::showRefreshProgress)
                            .doOnCompleted(getView()::hideRefreshProgress)
                            .subscribe(this::syncWithView,
                                    th -> {
                                        if (!isViewGone()) {
                                            getView().showMessage(th.getMessage());
                                            syncWithView(cache);
                                            getView().hideRefreshProgress();
                                        }
                                        Log.w(TAG, "error on fetch rss#", th);
                                    });
                } else {
                    if (!isViewGone()) {
                        syncWithView(cache);
                    }
                }
            } else {
                if (!isViewGone()) {
                    syncWithView(savedState.getParcelableArrayList(MainPresenter.ARG_CACHE));
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putParcelableArrayList(MainPresenter.ARG_CACHE, localCache);
    }

    @Override
    protected void onDestroyed() {
        itemChangedObsSubscr.unsubscribe();
    }

    private void syncWithView(Collection<RssItemModel> result) {

        if (isViewGone())
            throw new IllegalStateException("nothing to sync with");

        Preconditions.checkNotNull(result);

        final IMainView view = getView();

        localCache.clear();
        localCache.addAll(result);
        view.clear();
        view.addModel(result);
    }

}
