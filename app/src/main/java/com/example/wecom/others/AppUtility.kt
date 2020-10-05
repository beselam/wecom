package com.example.wecom.others

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object AppUtility {
    fun locationPermissions(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun createFormattedTime(ms: Long, includeMillis: Boolean = false): String {
        var milliseconds = ms
        val hr = TimeUnit.MILLISECONDS.toHours(milliseconds)
        milliseconds -= TimeUnit.HOURS.toMillis(hr)
        val min = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
        milliseconds -= TimeUnit.MINUTES.toMillis(min)
        val sec = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
        if(!includeMillis) {
            return "${if(hr < 10) "0" else ""}$hr:" +
                    "${if(min < 10) "0" else ""}$min:" +
                    "${if(sec < 10) "0" else ""}$sec"
        }
        milliseconds -= TimeUnit.SECONDS.toMillis(sec)
        milliseconds /= 10
        return "${if(hr < 10) "0" else ""}$hr:" +
                "${if(min < 10) "0" else ""}$min:" +
                "${if(sec < 10) "0" else ""}$sec:" +
                "${if(milliseconds < 10) "0" else ""}$milliseconds"
    }


    fun calculatePolylineLength(polyline: MutableList<LatLng>): Float {
        var distance = 0f
        for(i in 0..polyline.size - 2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }
    fun getFormatedDay():String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

        val date = dateFormat.format(calendar.time)
        return  date
    }


}