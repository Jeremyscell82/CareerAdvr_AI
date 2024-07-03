package com.lloydsbyte.core.permissions.location

import android.location.Location

interface LocationControllerListener {
    fun onLocationGathered(location: Location)
    fun onLocationNotEnabled()
    fun onFailedOut()
}