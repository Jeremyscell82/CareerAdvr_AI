package com.lloydsbyte.careeradvr_ai.analytics

import android.content.Context
import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import kotlin.math.cos


/**
 * This class is where all analytic calls should flow through, making it easy to change the format on a global scale
 */
class Analytix {


    private val EVENT_USED_AMA = "used_ama"
    private val EVENT_USED_PRO = "used_pro"
    private val MIX_ADS = "mix_ads"
    private val EVENT_AD_CLICKED = "event_ad_clicked"
    private val EVENT_AD_SHOWN = "event_ad_shown"
    private val EVENT_AD_FAILED = "event_ad_failed"


    //FIREBASE USAGE


    //MixPanel Usage
    /** ADS **/
    fun reportAmaUsed(appContext: Context) {
        MixPanelController().reportChatUsed(appContext, EVENT_USED_AMA, "ama")
    }

    fun reportProUsed(appContext: Context, proTitle: String) {
        MixPanelController().reportChatUsed(appContext, EVENT_USED_PRO, proTitle)
    }

    fun reportCost(appContext: Context, cost: String, adsShown: String) {
        MixPanelController().reportConvoCost(appContext, cost, adsShown)
    }


    /** ADS **/
    fun reportAdClicked(appContext: Context) {
        MixPanelController().reportUsageEvent(
            appContext = appContext,
            eventName = MIX_ADS,
            eventValueName = EVENT_AD_CLICKED,
            eventValue = "true"
        )
    }

    fun reportAdFailed(appContext: Context) {
        MixPanelController().reportUsageEvent(
            appContext = appContext,
            eventName = MIX_ADS,
            eventValueName = EVENT_AD_CLICKED,
            eventValue = "true"
        )
    }

    fun reportAdShown(appContext: Context) {
        MixPanelController().reportUsageEvent(
            appContext = appContext,
            eventName = MIX_ADS,
            eventValueName = EVENT_AD_CLICKED,
            eventValue = "true"
        )
    }

}