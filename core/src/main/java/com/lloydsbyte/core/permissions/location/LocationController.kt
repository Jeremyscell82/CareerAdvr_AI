package com.lloydsbyte.core.permissions.location

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.lloydsbyte.core.permissions.PermissionCheck

class LocationController(private val context: Context, private val controllerListener: LocationControllerListener):
    LocationListener { //Extends off fragment to act as a one but does not have it's own ui


    private val timerAmount = 15000L
    lateinit var locationService: LocationServices
    private var locationCountDownTimer = object : CountDownTimer(timerAmount, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            //We could do something here but... we don't
        }
        override fun onFinish() {
            //At this point let's call it quits and ask the user to check their settings.
            cancelLocation()
        }
    }

    //Just call this to get/ask/inform about location permission, include a dialog with the runnable
    fun getLocationInfo(deniedRunnable: Runnable) {
        if (PermissionCheck.isLocationGranted(context)) {
            //Location has been granted, determine which kind to use
            if (!LocationServices().isLocationEnabled(context)){
                controllerListener.onLocationNotEnabled()
            } else {
                //Get location based off GPS
                getLocation()
            }
        } else {
            deniedRunnable.run()
        }
    }

    private fun getLocation(){
        //Location enabled and permission has been granted
        var locationManager = context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        locationService = LocationServices()
        locationService.getLocation(locationManager, this)
        locationCountDownTimer.start()
    }

    private fun cancelLocation() {
        controllerListener.onFailedOut()
        dismissLocationRequest()
    }

    override fun onLocationChanged(location: Location) {
        controllerListener.onLocationGathered(location)
        dismissLocationRequest()
    }

    private fun dismissLocationRequest(){
        locationCountDownTimer.cancel()
        locationService.dismissUpdates()
    }
}