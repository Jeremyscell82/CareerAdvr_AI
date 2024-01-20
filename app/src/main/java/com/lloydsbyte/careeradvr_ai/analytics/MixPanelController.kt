package com.lloydsbyte.careeradvr_ai.analytics

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

/**
 * This class will be the front facing class that sends off MixPanel Analytics
 */
class MixPanelController : MixPanelConstants() {

    private val MIX_CHAT = "mix_chat"
    private val EVENT_CONVO_COST = "mix_convo_cost"

    private val EVENT_CONVO_ADS_SHOWN= "mix_convo_ads_shown"


    private fun getMixPanelInstance(appContext: Context): MixpanelAPI {
        return MixpanelAPI.getInstance(appContext.applicationContext,
            mixpanelId, false)
    }

    fun reportChatUsed(appContext: Context, eventName: String, eventVlaue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventName, eventVlaue)
        mixPanelInstance.track(MIX_CHAT, props)
    }
    fun reportConvoCost(appContext: Context, cost: String, adsShown: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(EVENT_CONVO_COST, cost)
        props.put(EVENT_CONVO_ADS_SHOWN, adsShown)
        mixPanelInstance.track(MIX_CHAT, props)
    }

    fun reportChatRating(appContext: Context, eventValueName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventValueName, eventValue)
        mixPanelInstance.track("EVENT_CHAT_RATING", props)
    }


    fun reportUsageEvent(appContext: Context, eventName: String, eventValueName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventValueName, eventValue)
        mixPanelInstance.track(eventName, props)
    }

    fun reportErrorEvent(appContext: Context,  eventValueName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventValueName, eventValue)
        mixPanelInstance.track(EVENT_ERROR, props)
    }
}