package com.lloydsbyte.core.utilz

import com.lloydsbyte.database.examples.CountryModel
import com.lloydsbyte.network.examples.CountryResponse

class ModelConverter {
    companion object {
        fun convertCountryResponse(rawData: List<CountryResponse.CountryResponse>): List<CountryModel> {
            return rawData.map {
                CountryModel(
                    dbKey = 0,
                    name = it.name.commonName,
                    capital = if (it.capital != null)it.capital!!.first() else "NA",
                    subregion = it.subregion,
                    region = it.region,
                    population = it.population
                )
            }
        }
    }
}