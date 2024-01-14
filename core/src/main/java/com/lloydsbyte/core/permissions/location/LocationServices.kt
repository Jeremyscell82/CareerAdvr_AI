package com.lloydsbyte.core.permissions.location

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import com.lloydsbyte.core.ErrorController

class LocationServices : Service(), LocationListener {

    var location: Location? = null
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null

    // The minimum distance to change Updates in meters
    val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F // 10 meters

    // The minimum time between updates in milliseconds
    val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute


    fun isLocationEnabled(context: Context): Boolean {
        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Checking GPS is enabled
        val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return mGPS
    }

    @SuppressWarnings("MissingPermission") //Permission Checked Prior to this call
    fun getLocation(locMgr: LocationManager, locLis: LocationListener) {
        locationManager = locMgr
        locationListener = locLis
        getGpsLocation(false)
    }

    fun dismissUpdates() {
        if (locationManager != null && locationListener != null)locationManager?.removeUpdates(locationListener!!)
    }

    @SuppressWarnings("MissingPermission") //Permission Checked Prior to this call
    private fun getNetworkLocation(useLastKnown: Boolean){
        // getting network status
        val isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        //If GPS is enabled, use GPS
        if (isNetworkEnabled==true) {
            // Get Location using network
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener!!
            )
            location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null && useLastKnown) {
                ErrorController.logStatus("JL_ Network -> lat: ${location?.latitude} and lon: ${location?.longitude}")
                locationListener?.onLocationChanged(location!!)
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun getGpsLocation(useLastKnown: Boolean){
        val isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGPSEnabled==true) {
            ErrorController.logStatus("JL_ Using GPS to get location")
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener!!
            )
            location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null && useLastKnown) {
                ErrorController.logStatus("JL_ current GPS -> lat: ${location?.latitude} and lon: ${location?.longitude}")
                locationListener?.onLocationChanged(location!!)
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLocationChanged(p0: Location) {

    }
}