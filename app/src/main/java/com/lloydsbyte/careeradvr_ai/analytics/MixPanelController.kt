package com.lloydsbyte.careeradvr_ai.analytics

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

/**
 * This class will be the front facing class that sends off MixPanel Analytics
 */
class MixPanelController : MixPanelConstants() {

    private val mixpanel_cost = "mix_panel"
    private val mixpanel_event_cost_0 = "mix_cost_nan"
    private val mixpanel_event_cost_0_1000 = "mix_cost_0-1000"
    private val mixpanel_event_cost_1001_2000 = "mix_cost_1001-2000"
    private val mixpanel_event_cost_2001_3000 = "mix_cost_2001-3000"
    private val mixpanel_event_cost_3001_5000 = "mix_cost_3001-5000"
    private val mixpanel_event_cost_5001_10000 = "mix_cost_5001-10000"
    private val mixpanel_event_cost_toomuch = "mix_cost_10001_"

    companion object {
        val mixpanel_event_first_timer = "mix_new_user"
        val mixpanel_event_new_chat = "mix_chat_new"
        val mixpanel_event_chat_subj_general = "mix_chat_subj_general"
        val mixpanel_event_chat_subj_history = "mix_chat_subj_history"
        val mixpanel_event_chat_subj_obj_tuner = "mix_chat_subj_obj_tuner"
        val mixpanel_event_chat_subj_obj_creator =  "mix_chat_subj_obj_creator"
        val mixpanel_event_chat_subj_cv = "mix_chat_subj_cv"
        val mixpanel_event_chat_subj_interview_tips = "mix_chat_subj_interview_tips"
        val mixpanel_event_chat_subj_mock_interview = "mix_chat_subj_mock_interview"
        val mixpanel_event_chat_subj_career_advice = "mix_chat_subj_career_adv"
    }


    private fun getMixPanelInstance(appContext: Context): MixpanelAPI {
        return MixpanelAPI.getInstance(appContext.applicationContext,
            mixpanelId, true)
    }

    //Call this function before resetting the users daily count
    fun reportUsage(appContext: Context, count: Int) {
        val mixPanelInstance = getMixPanelInstance(appContext)
        val prop = JSONObject()
        prop.put(getUsageCategory(count), count)
        mixPanelInstance.track(mixpanel_cost, prop)
    }

    private fun getUsageCategory(count: Int): String {
        return when {
            count in 1..1000 -> mixpanel_event_cost_0_1000
            count in 1001..2000 -> mixpanel_event_cost_1001_2000
            count in 2001..3000 -> mixpanel_event_cost_2001_3000
            count in 3001..5000 -> mixpanel_event_cost_3001_5000
            count in 5001..10000 -> mixpanel_event_cost_5001_10000
            count > 10000 -> mixpanel_event_cost_toomuch
            else -> mixpanel_event_cost_0
        }
    }






    /** OLD CODE **/
    private val MIX_CHAT = "mix_chat"
    private val EVENT_CONVO_COST = "mix_convo_cost"

    private val EVENT_CONVO_ADS_SHOWN= "mix_convo_ads_shown"
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