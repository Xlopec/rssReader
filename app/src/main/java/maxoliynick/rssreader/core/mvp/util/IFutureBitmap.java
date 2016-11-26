package maxoliynick.rssreader.core.mvp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.annotation.Px;

import org.jetbrains.annotations.NotNull;

import rx.Observable;

/**
 * Created by Максим on 11/25/2016.
 */

public interface IFutureBitmap extends Parcelable {

    @NotNull
    Observable<Bitmap> fetch(@Px int width, @Px int height, @NotNull Context context);

}
