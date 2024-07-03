package com.lloydsbyte.network.examples

import com.google.gson.annotations.SerializedName

class CountryResponse {
    data class CountryResponse(
        @SerializedName("name")
        val name: Name,
        @SerializedName("capital")
        val capital: List<String>?,
        @SerializedName("subregion")
        val subregion: String,
        @SerializedName("region")
        val region: String,
        @SerializedName("population")
        val population: Long
    )

    data class Name(
        @SerializedName("common")
        val commonName: String
    )
}