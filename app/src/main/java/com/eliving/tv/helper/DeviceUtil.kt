/*
 * *
 *  * Created by DinhDV on 8/10/18 9:14 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 8/10/18 9:14 AM
 *
 */

package com.eliving.tv.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.wifi.WifiManager
import android.provider.Settings
import com.eliving.tv.AppContext
import java.util.*


object DeviceUtil {

    @SuppressLint("HardwareIds")
    fun deviceID(): String {

        /*return "b318abb5f7beab5&"*/

        return Settings.System.getString(AppContext.contentResolver,
            Settings.Secure.ANDROID_ID)
    }

    @SuppressLint("HardwareIds")
    fun macID(): String {

        /*return "020000000009"*/

        val manager = AppContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        return info.macAddress.toUpperCase(Locale.ROOT).replace(":","")
    }

    fun getVersionCode(): String {
        var version = "";
        try {
            val pm = AppContext.applicationContext.packageManager
            val pInfo =
                pm.getPackageInfo("com.google.android.apps.photos", 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return version;
    }
    fun screenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun screenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun dpsToPixel(dps: Int): Int {
        return (dps * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }


}