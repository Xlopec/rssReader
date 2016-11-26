package maxoliynick.rssreader.core.mvp.core;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

/**
 * <p>
 *     This interface represents
 *     basic API for each View component
 *     in MVP pattern
 * </p>
 * Created by Максим on 5/25/2016.
 */
public interface IBaseView {
    /**
     * Returns view's context
     * */
    @NotNull
    Context getContext();
    /**
     * Returns app's context
     * */
    @NotNull
    Context getApplicationContext();

}
