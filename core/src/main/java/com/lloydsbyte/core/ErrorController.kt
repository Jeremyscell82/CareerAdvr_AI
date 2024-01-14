package com.lloydsbyte.core

import com.afollestad.materialdialogs.BuildConfig
import timber.log.Timber

/**
 * This class helps funnel all errors  from all libraries into one place making switching Analytics/Crashlytics/Logging that much easier
 */
class ErrorController {
    companion object {

        fun captureError(location: String, error: java.lang.Exception) {
            val errorCaptured = "JL_  $location :: ${error.localizedMessage}"
            //Here get data to retrieve for crashes???


            logError(errorCaptured)
        }

        fun captureError(location: String, error: Throwable) {
            val error2 = "JL_  $location :: ${error.localizedMessage}"
            logError(error2)
        }

        fun logError(error: String?) {
            if (BuildConfig.DEBUG)Timber.d("JL_ logging error: $error")
        }

        fun logError(error: Throwable) {
            if (BuildConfig.DEBUG)Timber.d("JL_ logging error: $error")
        }

        fun logStatus(message: String) {
            if (BuildConfig.DEBUG)Timber.d("JL_ logging status: $message")
        }

    }
}