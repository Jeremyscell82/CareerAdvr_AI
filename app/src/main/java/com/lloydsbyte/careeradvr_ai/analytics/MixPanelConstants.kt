package com.lloydsbyte.careeradvr_ai.analytics

open class MixPanelConstants {
    companion object {
        const val mixpanelId = "07a44ee5b2d08818752203096c9d9fc4"

        val MIX_USAGE = "app_usage"
        val MIX_ERROR = "app_error"

        const val EVENT_AD_CLICKED = "ad_clicked"
        const val EVENT_AD_FAILED = "ad_failed"

        const val EVEN_SETTINGS = "Settings"
        const val VALUE_GPT_MODEL = "Gpt Model"
        const val VALUE_TEMP = "Gpt Temp"


        const val EVENT_TOKEN = "Tokens"
        const val VALUE_CONVO = "Conversation Tokens"
        const val VALUE_QUESTIONS = "Questions Asked"

        const val EVENT_CHAT = "Chat Usage"
        const val VALUE_PRO = "Profession Used"
        const val VALUE_AMA = "AMA Used"

        const val EVENT_CHAT_RATING = "Chat Rating"
        const val VALUE_AMA_RATING = "Ama Rating"
        const val VALUE_PRO_RATING = "Pro Rating"

        const val EVENT_ERROR = "Error"
        const val VALUE_ERROR_CHAT = "Gpt Error"

    }
}