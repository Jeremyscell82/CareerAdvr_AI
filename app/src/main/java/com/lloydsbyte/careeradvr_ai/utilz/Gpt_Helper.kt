package com.lloydsbyte.careeradvr_ai.utilz

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lloydsbyte.core.utilz.StoredPref
import com.lloydsbyte.core.utilz.UtilzDateHelper
import com.lloydsbyte.database.models.ChatModel
import com.lloydsbyte.network.NetworkConstants

//This class holds all the helper functions for the api
class Gpt_Helper {
    companion object {
        val user = "user"
        val system = "system"
        val assistant = "assistant"
    }
    /** NEW VERSION OF CHAT GPT **/

    fun promptCreator(context: Context, defaultPrompt: String, title: String?, prompt: String?): String {
        var preppedPrompt = "$defaultPrompt Your clients name is ${StoredPref(context).getUserName()}, they are ${StoredPref(context).getUserAge()} years old."
        if (!title.isNullOrEmpty() && !prompt.isNullOrEmpty()) preppedPrompt = "$preppedPrompt They would like to know information specific to $title. $prompt"
        return preppedPrompt
    }

    fun createSystemPrompt(convoId: Long, systemPrompt: String): ChatModel {
        return ChatModel(
            dbKey = 0,
            conversationId = convoId,
            timestamp = convoId,
            role = system,
            content = systemPrompt
        )
    }

    fun prepPayload(activity: Context, gptModel: String, convo: List<ChatModel>, systemPrompt: ChatModel?): JsonObject {
        val conversation: MutableList<ChatModel> = mutableListOf()
        if (systemPrompt != null){
            conversation.add(0, systemPrompt)
        }
        conversation.addAll(convo)
        return createJsonPayload(activity, gptModel, conversation)
    }

    private fun createJsonPayload(activity: Context, gptModel: String, questions: List<ChatModel>): JsonObject {
        val payload = JsonObject()
        val prefs = StoredPref(activity)
        payload.addProperty("model", gptModel)
        payload.addProperty("temperature", prefs.chatTemp())

        //setup question
        val questionPayload: MutableList<JsonObject> = mutableListOf()
        questions.map {
            val question = JsonObject()
            question.addProperty("role", it.role)
            question.addProperty("content", it.content)
            questionPayload.add(question)
        }
        payload.add("messages", Gson().toJsonTree(questionPayload))
        return payload
    }


    fun createChatModel(convoId: Long, role: String, content: String): ChatModel {
        return ChatModel(
            dbKey = 0,
            conversationId = convoId,
            timestamp = UtilzDateHelper(UtilzDateHelper.DF_TIMEDATE).buildMillisDate(),
            role = role,
            content = content
        )
    }

    fun createSharableConversation(convo: List<ChatModel>): String {
        var sharableStr = ""
        convo.map { model ->
            if (model.role != system) sharableStr = "$sharableStr\n${model.role}:\n${model.content}"
        }
        return sharableStr.trim()
    }

    /** DALL-E FUNCTIONS **/
    fun createDallEPayload(activity: Activity, phrase: String): JsonObject {
        val payload = JsonObject()
        payload.addProperty("prompt", phrase)
        payload.addProperty("n", 1)
        payload.addProperty("size", "512x512")
        return payload
    }

    //Model Picker Converters
    fun convertSelectedModel(itemSelected: Int) : String {
        return when(itemSelected){
            //Gpt 3 Turbo
            //Gpt 4
            1 -> NetworkConstants.gpt_4
            //Gpt 4 Turbo
            2 -> NetworkConstants.gpt_4_turbo
            else -> NetworkConstants.gpt_4
        }
    }

    fun convertModelForLegibility(modelName: String, items: List<String>): String {
        return when(modelName){
            //Gpt 3 Turbo
            //Gpt 4
            NetworkConstants.gpt_4 -> items[1]
            //Gpt 4 Turbo
            NetworkConstants.gpt_4_turbo -> items[2]
            else -> NetworkConstants.gpt_4
        }
    }

}