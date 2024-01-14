package com.lloydsbyte.database.examples

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class CountryModel(
    @PrimaryKey(autoGenerate = true)
    val dbKey: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "capital")
    val capital: String,
    @ColumnInfo(name = "subregion")
    val subregion: String?,
    @ColumnInfo(name = "region")
    val region: String,
    @ColumnInfo(name = "population")
    val population: Long
): Parcelable
