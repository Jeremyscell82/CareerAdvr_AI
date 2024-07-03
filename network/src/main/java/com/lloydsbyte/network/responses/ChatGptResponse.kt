package com.lloydsbyte.network.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class ChatGptResponse {

    @Parcelize
    data class GptData(
        @SerializedName("id")
        val id: String,
        @SerializedName("object")
        val obj: String,
        @SerializedName("created")
        val createdDateMillis: Long,
        @SerializedName("model")
        val model: String,
        @SerializedName("usage")
        val usage: GptUsage,
        @SerializedName("choices")
        val choices: List<GptChoice>
    ): Parcelable

    @Parcelize
    data class GptUsage(
        @SerializedName("prompt_tokens")
        val promptTokens: Int,
        @SerializedName("completion_tokens")
        val completionTokens: Int,
        @SerializedName("total_tokens")
        val totalTokens: Int
    ): Parcelable

    @Parcelize
    data class GptChoice(
        @SerializedName("message")
        val message: GptMessage,
        @SerializedName("delta")
        val delta: GptMessage,
        @SerializedName("finish_reason")
        val finishReason: String,
        @SerializedName("index")
        val index: Int
    ): Parcelable

    @Parcelize
    data class GptMessage(
        @SerializedName("role")
        val role: String,
        @SerializedName("content")
        val content: String
    ): Parcelable


}