package framgia.vn.weatherforecast.util;

import android.content.Context;

/**
 * Created by toannguyen201194 on 29/06/2016.
 */
public class ResourcesUtils {
    public static int getResources(Context mContext, String nameImage, String drawable) {
        return mContext.getResources()
            .getIdentifier(nameImage, drawable, mContext.getPackageName());
    }
}
