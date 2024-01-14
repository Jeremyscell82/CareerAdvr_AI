package com.lloydsbyte.network.responses

import com.google.gson.annotations.SerializedName

class ModelResponse {

    data class ModelData(
        @SerializedName("data")
        val data: List<AiModel>
    )

    data class AiModel(
        @SerializedName("id")
        val id: String,
        @SerializedName("object")
        val obj: String,
        @SerializedName("owned_by")
        val ownedBy: String
    )
}