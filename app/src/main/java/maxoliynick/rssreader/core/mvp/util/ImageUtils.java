package maxoliynick.rssreader.core.mvp.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * <p>
 *     Source code was taken from article
 *     <a href="https://developer.android.com/training/displaying-bitmaps/load-bitmap.html">Loading Large Bitmaps Efficiently</a>
 * </p>
 * Created by Максим on 7/9/2016.
 */
public final class ImageUtils {

    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    private ImageUtils() {}

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static void setScaledImage(final Resources res, final ImageView imageView, final int resId) {

        final ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();

        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw() {

                imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                final int imageViewHeight = imageView.getMeasuredHeight();
                final int imageViewWidth = imageView.getMeasuredWidth();

                imageView.setImageBitmap(
                        decodeSampledBitmapFromResource(res, resId, imageViewWidth, imageViewHeight)
                );
                return true;
            }
        });
    }

    public static int pxToDp(int px) {
        return (int) (px / ImageUtils.DENSITY);
    }

    public static int dpToPx(int dp) {
        return (int) ImageUtils.DENSITY * dp;
    }

    public static int spToPx(int sp) {
        return (int) (sp * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

}
