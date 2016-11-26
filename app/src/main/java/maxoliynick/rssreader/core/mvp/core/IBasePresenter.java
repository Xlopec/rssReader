package maxoliynick.rssreader.core.mvp.core;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * <p>
 * Represents base presenter in MVP pattern
 * </p>
 * Created by Максим on 5/25/2016.
 */
public abstract class IBasePresenter<View extends IBaseView> {

    private static final String TAG = IBasePresenter.class.getSimpleName();

    private volatile boolean isFirstTimeAttached;

    private volatile boolean isAttached;
    /*
    weak reference allows release view in cases
    when system destroys attached view
    */
    private WeakReference<View> viewWeakReference;

    public IBasePresenter() {
        this.isFirstTimeAttached = true;
    }

    /**
     * Defines whether view is not present
     */
    protected final boolean isViewGone() {
        return viewWeakReference == null || viewWeakReference.get() == null || !isAttached;
    }

    /**
     * Defines whether view is attached first time or not. Returned
     * value will be reset to its initial state after call of
     * {@code onSaveState} method or after new instance of presenter
     * was created
     */
    protected final boolean isFirstTimeAttached() {
        return isFirstTimeAttached;
    }

    /**
     * Defines whether view was attached
     */
    protected final boolean isAttached() {
        return isAttached;
    }

    /**
     * Returns view which was set in {@code attachView} method
     */
    protected final View getView() {
        return viewWeakReference == null ? null : viewWeakReference.get();
    }

    /**
     * Attaches specified view to presenter. After call of this method
     * {@code onAttachView} and {@code onCreateViewState} will
     * be invoked. After call of this method the {@code isFirstTimeAttached}
     * will return <b>false</b>
     *
     * @param view a view to attach
     * @param data data passed via view
     */
    public final void attachView(@NotNull View view, @Nullable Bundle data, @Nullable Bundle savedState) {

        viewWeakReference = new WeakReference<>(view);
        isAttached = true;

        try {
            onViewAttached(view, savedState, data);
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred in onAttachView!", e);
        } finally {
            isFirstTimeAttached = false;
        }
    }

    /**
     * This method should be called when presenter
     * has to be destroyed
     */
    public final void onDestroy() {

        try {

            if (viewWeakReference != null) {
                viewWeakReference.clear();
            }
            onDestroyed();
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred in onSaveState!", e);
        } finally {
            isFirstTimeAttached = true;
            isAttached = false;
        }
    }

    /**
     * This method should handle view attachment event, it'll be called before
     * {@code onCreateViewState}. In most cases this method just sets view's fields
     * with plain data
     *
     * @param view       attached view
     * @param savedState saved view state which was passed via {@code onSaveInstanceState}
     * @param data       data passed with view
     */
    protected abstract void onViewAttached(@NotNull View view, @Nullable Bundle savedState, @Nullable Bundle data);

    /**
     * Handle presenter destruction event. The implementation
     * of this method should release all caches, connections and so on
     * to avoid memory leaks
     */
    protected abstract void onDestroyed();

    /**
     * Saves presenter's state. This method should be called
     * by OS only!
     *
     * @param outState bundle to save state in
     */
    public void onSaveInstanceState(@NotNull Bundle outState) {
    }

    /**
     * Called when corresponding activity or fragment calls #onResume()
     */
    public void onResume() {
    }

    /**
     * Called when corresponding activity or fragment calls #onPause()
     */
    public void onPause() {
    }
}
