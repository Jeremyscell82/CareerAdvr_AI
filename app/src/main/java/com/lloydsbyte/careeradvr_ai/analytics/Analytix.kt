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


    private val EVENT_USED_AMA = "used_chat_subject_ama"
    private val EVENT_USED_PRO = "mix_chat_subject_selected"
    private val MIX_ADS = "mix_ads"
    private val EVENT_AD_CLICKED = "event_ad_clicked"
    private val EVENT_AD_SHOWN = "event_ad_shown"
    private val EVENT_AD_FAILED = "event_ad_failed"


    //FIREBASE USAGE


    /** MIXPANEL **/
    fun reportTokenCount(appContext: Context, tokenCount: Int) {
        MixPanelController().reportUsage(appContext, tokenCount)
    }

    fun reportAmaUsed(appContext: Context) {
        MixPanelController().reportChatUsed(appContext, EVENT_USED_AMA, MixPanelController.mixpanel_event_chat_subj_general)
    }

    fun reportProUsed(appContext: Context, proId: Int) {
        MixPanelController().reportChatUsed(appContext, EVENT_USED_PRO, getProEventName(proId))
    }

    private fun getProEventName(position: Int): String {
        return when (position) {
            0->MixPanelController.mixpanel_event_chat_subj_history
            1->MixPanelController.mixpanel_event_chat_subj_obj_tuner
            2->MixPanelController.mixpanel_event_chat_subj_obj_creator
            3->MixPanelController.mixpanel_event_chat_subj_cv
            4->MixPanelController.mixpanel_event_chat_subj_interview_tips
            5->MixPanelController.mixpanel_event_chat_subj_mock_interview
            6->MixPanelController.mixpanel_event_chat_subj_career_advice
            else->MixPanelController.mixpanel_event_chat_subj_general
        }
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