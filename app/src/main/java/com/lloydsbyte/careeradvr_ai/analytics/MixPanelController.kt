package com.lloydsbyte.careeradvr_ai.analytics

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

/**
 * This class will be the front facing class that sends off MixPanel Analytics
 */
class MixPanelController : MixPanelConstants() {
    companion object {

        const val EVENT_USED_AMA = "used_ama"
        const val EVENT_USED_SETTINGS = "used_settings"
        const val EVENT_USED_GPT_MODEL = "used_gpt_model"
        const val EVENT_USED_GPT_TEMP = "used_gpt_temp"
        const val EVENT_USED_SAVED = "used_saved"
        const val EVENT_USED_SHARED = "used_shared"

    }

    private fun getMixPanelInstance(appContext: Context): MixpanelAPI {
        return MixpanelAPI.getInstance(appContext.applicationContext,
            mixpanelId, false)
    }

    fun reportUsageEvent(appContext: Context, eventName: String, eventValueName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventValueName, eventValue)
        mixPanelInstance.track(eventName, props)
    }

    fun reportChatRating(appContext: Context, eventValueName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventValueName, eventValue)
        mixPanelInstance.track(EVENT_CHAT_RATING, props)
    }

    fun reportErrorEvent(appContext: Context,  eventValueName: String, eventValue: String) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val props = JSONObject()
        props.put(eventValueName, eventValue)
        mixPanelInstance.track(EVENT_ERROR, props)
    }
}