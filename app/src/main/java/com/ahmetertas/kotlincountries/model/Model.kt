package com.ahmetertas.kotlincountries.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// Burada room database'i için tabloları annotation'lar aracılığı ile gösterdik. Sütun isimlerini vs verdik. Aşağıda da primary Key'i normal değişken olarak verdik.
@Entity
data class Country (

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val countryName: String?,

    @ColumnInfo(name = "region")
    @SerializedName("region")
    val countryRegion: String?,

    @ColumnInfo(name = "capital")
    @SerializedName("capital")
    val countryCapital: String?,

    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    val countryCurrency: String?,

    @ColumnInfo(name = "language")
    @SerializedName("language")
    val countryLanguage: String?,

    @ColumnInfo(name = "flag")
    @SerializedName("flag")
    val countryImage: String?
    ) {

    // Böyle de primaryKey tanımladık.
    @PrimaryKey(autoGenerate = true)
    var uId : Int = 0

}