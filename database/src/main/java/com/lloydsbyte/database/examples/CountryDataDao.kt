package com.lloydsbyte.database.examples

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface CountryDataDao {

    @Insert
    fun addCountriesData(locationDataList: List<CountryModel>) : Completable

    @Query("SELECT * FROM countrymodel WHERE name IS :name")
    fun getSpecificCountry(name: String): Flowable<List<CountryModel>>


    @Query("SELECT COUNT(*) FROM countrymodel")
    fun getCount(): Flowable<Int>

    @Query("SELECT * FROM countrymodel")
    fun readAllCountries(): Flowable<List<CountryModel>>

    @Query("DELETE FROM countrymodel")
    fun deleteLocationDB(): Completable

}