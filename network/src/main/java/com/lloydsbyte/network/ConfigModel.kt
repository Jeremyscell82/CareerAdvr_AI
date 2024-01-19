package com.lloydsbyte.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import javax.crypto.SecretKey

class ConfigModel {

    data class BaseStructure(
        @SerializedName("version")
        val version: String,
        @SerializedName("min_build")
        val minBuild: Int,
        @SerializedName("promo_banner")
        val promoBanner: List<PromoBanner>,
        @SerializedName("secret_key")
        val secretKey: String,
        @SerializedName("ama_prompt")
        val defaultPrompt: String,
        @SerializedName("prompt_list")
        val promptList: List<Prompt>
    )

    data class PromoBanner(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("active")
        val active: Boolean,
        @SerializedName("code")
        val promoCode: String
    )

    @Parcelize
    data class Prompt(
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("prompt")
        val systemPrompt: String,
        @SerializedName("paywall")
        val payWall: Boolean
    ) : Parcelable

}