package com.lloydsbyte.careeradvr_ai.analytics

import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

/**
 * This class will the the controller for firebase analytics
 */
class FirebaseController {
    companion object {
        val EVENT_AD_CLICKED = "ad_clicked"
        val EVENT_AD_FAILED = "ad_failed"
        val EVENT_APP_FEEDBACK = "app_feedback_sent"
    }

    fun reportAdClicked() {
        reportFirebaseEvent(EVENT_AD_CLICKED, "true")
    }

    fun reportAdError() {
        reportFirebaseEvent(EVENT_AD_FAILED, "true")
    }

    private fun reportFirebaseEvent(eventName: String, appVersion: String){
        Firebase.analytics.logEvent(eventName, packageParams(appVersion))
    }

    private fun packageParams(appversion: String): Bundle {
        val bundle = Bundle()
        bundle.putString("app_version", appversion)
        bundle.putString("os_version", Build.VERSION.RELEASE)
        bundle.putString("model_version", Build.MODEL)
        return bundle
    }
}