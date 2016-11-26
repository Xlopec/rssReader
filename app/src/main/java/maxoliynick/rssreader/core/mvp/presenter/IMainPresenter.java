package maxoliynick.rssreader.core.mvp.presenter;

import org.jetbrains.annotations.NotNull;

import maxoliynick.rssreader.core.mvp.core.IBasePresenter;
import maxoliynick.rssreader.core.mvp.model.RssItemModel;
import maxoliynick.rssreader.core.mvp.view.IMainView;

/**
 * Created by Максим on 11/25/2016.
 */

public abstract class IMainPresenter extends IBasePresenter<IMainView> {

    public abstract void onItemClick(@NotNull RssItemModel itemModel);

    public abstract void onRefresh();

}
