package com.lloydsbyte.core.assetloader

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class AppConfigModel {

    data class AppConfigFileResponse(
        @SerializedName("date_updated")
        val dateUpdated: String,
        @SerializedName("home_items")
        val homeItems: List<HomeItem>,
        @SerializedName("home_professions")
        val professions: List<String>
//        @SerializedName("notification")
//        val customAd: CustomNotification
    )

    @Parcelize
    data class HomeItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("filter_title")
        val filterTitle: String,
        @SerializedName("selected")
        var selected: Boolean,
        @SerializedName("body_message")
        val bodyMessage: String,
        @SerializedName("hint")
        val hint: String,
        @SerializedName("options")
        val options: List<String>,
        @SerializedName("system_prompt")
        val systemPrompt: String
    ): Parcelable


    //To be used to inform the customer of a current special or seasonal greetings
//    @Parcelize
//    data class CustomNotification(
//        @SerializedName("id")
//        val id: Int,
//        @SerializedName("active")
//        val active: Boolean,
//        @SerializedName("title")
//        val title: String,
//        @SerializedName("body")
//        val body: String,
//        @SerializedName("body_img_url")
//        val imgUrl: String,
//        @SerializedName("action_btn")
//        val actionBtn: String,
//        @SerializedName("action_url")
//        val actionUrl: String
//    ) : Parcelable

}