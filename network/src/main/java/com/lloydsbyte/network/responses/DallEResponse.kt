package com.lloydsbyte.network.responses

import com.google.gson.annotations.SerializedName

class DallEResponse {

    data class DallEData(
        @SerializedName("created")
        val created: String,
        @SerializedName("data")
        val urlDataList: List<UrlData>
    )

    data class UrlData(
        @SerializedName("url")
        val imageUrl: String
    )
}