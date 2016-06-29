package framgia.vn.weatherforecast.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nguyen van toan on 5/26/2016.
 */
public final class CheckConnectionUtil {
    public static boolean isInternetOn(Context mContext) {
        int mobiState = ConnectivityManager.TYPE_MOBILE;
        int wifiState = ConnectivityManager.TYPE_WIFI;
        ConnectivityManager connec = (ConnectivityManager)
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.getNetworkInfo(mobiState).getState() == NetworkInfo.State.CONNECTED ||
            connec.getNetworkInfo(mobiState).getState() == android.net.NetworkInfo.State.CONNECTING ||
            connec.getNetworkInfo(wifiState).getState() == android.net.NetworkInfo.State.CONNECTING ||
            connec.getNetworkInfo(wifiState).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
            connec.getNetworkInfo(mobiState).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(wifiState).getState() ==
                    android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }
}