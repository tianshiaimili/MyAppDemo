
package com.hua.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtils {
	/**就爱那个dp converted to px*/
    public static int convertDpToPixel(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
}
