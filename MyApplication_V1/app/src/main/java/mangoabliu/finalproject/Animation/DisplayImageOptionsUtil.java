package mangoabliu.finalproject.Animation;

/**
 * Created by Byanka on 26/11/2016.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import mangoabliu.finalproject.R;

public final class DisplayImageOptionsUtil {

    /**
     * 获取图片显示选项对象实体
     *
     * @return 图片显示选项
     */
    public static DisplayImageOptions getDisplayImageOptions() {

        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.grey_bg)
                .showImageOnFail(R.drawable.grey_bg)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();

        return options;
    }

    /**
     * 获取图片显示Listener
     *
     * @return Listener实体
     */
    public static ImageLoadingListener getImageLoadingListener() {
        return new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                if (arg1 != null && arg1.getTag() != null && arg1.getTag() instanceof ProgressBar) {
                    ((ProgressBar) arg1.getTag()).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                if (arg1 != null && arg1.getTag() != null && arg1.getTag() instanceof ProgressBar) {
                    ((ProgressBar) arg1.getTag()).setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                if (arg1 != null && arg1.getTag() != null && arg1.getTag() instanceof ProgressBar) {
                    ((ProgressBar) arg1.getTag()).setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                if (arg1 != null && arg1.getTag() != null && arg1.getTag() instanceof ProgressBar) {
                    ((ProgressBar) arg1.getTag()).setVisibility(View.GONE);
                }
            }
        };
    }

}
