package com.lloydsbyte.network.examples

interface CountryInterface {
    fun onPullCompleted(countryResponseList: List<CountryResponse.CountryResponse>)
    fun onPullFailed(error: Throwable)
}