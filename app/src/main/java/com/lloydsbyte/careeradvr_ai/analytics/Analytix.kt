package com.lloydsbyte.careeradvr_ai.analytics

import android.content.Context
import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject


/**
 * This class is where all analytic calls should flow through, making it easy to change the format on a global scale
 */
class Analytix {

    //FIREBASE USAGE
    fun reportFirebaseEvent(eventName: String) {
        Firebase.analytics.logEvent(eventName, packageParams())
    }
    private fun packageParams(): Bundle {
        val bundle = Bundle()
        bundle.putString("app_version", BuildConfig.VERSION_NAME)
        bundle.putString("os_version", Build.VERSION.RELEASE)
        bundle.putString("model_version", Build.MODEL)
        return bundle
    }
    //MixPanel Usage
    private fun getMixPanelInstance(appContext: Context): MixpanelAPI {
        return MixpanelAPI.getInstance(appContext, MixPanelConstants.mixpanelId, false)
    }

    fun reportUsageEvent(appContext: Context, eventName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventName, eventValue)
        mixPanelInstance.track(MixPanelConstants.MIX_USAGE, props)
    }

    fun reportErrorEvent(appContext: Context, eventName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventName, eventValue)
        mixPanelInstance.track(MixPanelConstants.MIX_ERROR, props)
    }

}