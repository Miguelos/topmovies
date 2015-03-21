package me.miguelos.topmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This class contains utility methods for the app.
 *
 * @author Miguel González Pérez
 * @since 14 Aug 2013 11:52:45
 */
public class Utils {

    /**
     * Check if there is any Internet connection active.
     *
     * @param context
     * @return "true" if the network is connected.
     */
    public static boolean isNetworkConected(Context context) {
        ConnectivityManager conMngr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMngr.getActiveNetworkInfo();
        return ((netInfo != null) && (netInfo.isConnected()));
    }
}
