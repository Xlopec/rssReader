package maxoliynick.rssreader.core.di;

import com.google.inject.AbstractModule;

import maxoliynick.rssreader.core.mvp.model.IMainModel;
import maxoliynick.rssreader.core.mvp.model.MainModel;
import maxoliynick.rssreader.core.mvp.presenter.IMainPresenter;
import maxoliynick.rssreader.core.mvp.presenter.MainPresenter;
import maxoliynick.rssreader.core.rss.IRssAppService;
import maxoliynick.rssreader.core.rss.IRssDomain;
import maxoliynick.rssreader.core.rss.RssAppService;
import maxoliynick.rssreader.core.rss.RssDomain;
import maxoliynick.rssreader.core.rss.api.ApiProvider;
import maxoliynick.rssreader.core.rss.api.IApiProvider;
import maxoliynick.rssreader.core.rss.storage.IRssStorage;
import maxoliynick.rssreader.core.rss.storage.RssStorage;

/**
 * <p>
 *     Class to configure Roboguice dependency injection
 * </p>
 * Created by Максим on 11/25/2016.
 */

public final class RoboBindings extends AbstractModule {

    @Override
    protected void configure() {
        bind(IApiProvider.class).to(ApiProvider.class);
        bind(IRssAppService.class).to(RssAppService.class);
        bind(IRssDomain.class).to(RssDomain.class);
        bind(IRssStorage.class).to(RssStorage.class);
        bind(IMainModel.class).to(MainModel.class);
        bind(IMainPresenter.class).to(MainPresenter.class);
    }
}
