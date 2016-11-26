package maxoliynick.rssreader.core.mvp.view;

import android.content.Intent;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import maxoliynick.rssreader.core.mvp.core.IBaseView;
import maxoliynick.rssreader.core.mvp.model.RssItemModel;

/**
 * Created by Максим on 11/25/2016.
 */

public interface IMainView extends IBaseView {

    void addModel(@NotNull Collection<RssItemModel> itemModels);

    void setModel(@NotNull Collection<RssItemModel> itemModels);

    void hideRefreshProgress();

    void clear();

    void showRefreshProgress();

    void showMessage(String message);

    void startActivity(@NotNull Intent intent);
}
