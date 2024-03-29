package com.lloydsbyte.network

class NetworkConstants {
    companion object {
        const val countriesApi = "https://restcountries.com/v3.1/"
        //GPT CODE
        const val ai_key = "Bearer sk-vwriCqNczWTYQLrqQLQXT"

        //CONFIG FILE
        const val serverBaseUrl = "https://lloydsbyte.com/cloud/"
        const val configFileEndpoint = "appkit/careeradvr_config.json"

        const val gptBaseUrl = "https://api.openai.com/v1/"
        const val aiModelsEndpoint = "models"
        const val chatGptEndpoint = "chat/completions"
        const val imageGptEndpoint = "images/generations"
        //Default Values for Stored Preferences (Will be able to override in future versions)
        const val temperature: Double = 0.3
        const val gpt_4_turbo = "gpt-4-turbo-preview"
        const val gpt_4 = "gpt-4"

    }
}