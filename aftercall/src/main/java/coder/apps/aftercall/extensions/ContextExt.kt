package coder.apps.aftercall.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import coder.apps.space.library.helper.TinyDB

fun isBannerLoad(i: Int): Boolean {
    return true
}

fun isNativeLoad(i: Int): Boolean {
    return false
}

fun checkNotificationPermission(context: Context): Boolean {
    return Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(
        context,
        "android.permission.POST_NOTIFICATIONS"
    ) == 0
}

fun Context.isNetworkOn(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    val hasInternet =
        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    val hasTransport = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    return hasInternet && hasTransport
}
