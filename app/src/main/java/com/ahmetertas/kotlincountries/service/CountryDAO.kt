package com.ahmetertas.kotlincountries.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ahmetertas.kotlincountries.model.Country

@Dao
interface CountryDAO {

    // Burada async şekilde bir insert işlemi tanımladık ve geriye eklediğimiz verilerin id'lerini dönüyor. vararg keyword'u ise parametre olarak istediğimiz kadar country nesnesi verebiliriz demek oluyor.
    @Insert
    suspend fun insertAll(vararg countries: Country) : List<Long>

    // Query annatation'ı ile istediğimzi query'leri yazabiliyoruz.
    @Query("SELECT * FROM country")
    suspend fun getAllCountries() : List<Country>

    @Query("SELECT * FROM country WHERE uId = :countryId")
    suspend fun getCountry(countryId: Int) : Country

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()

}