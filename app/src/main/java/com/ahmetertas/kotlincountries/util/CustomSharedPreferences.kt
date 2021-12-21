package com.ahmetertas.kotlincountries.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class CustomSharedPreferences {

    companion object {

        // Burada yine baseViewModel'deki gibi singleton yapı kuruyoruz.
        private val PREFERENCES_TIME = "preferences_time"
        private var sharedPreferences : SharedPreferences? = null

        @Volatile private var instance : CustomSharedPreferences? = null
        private val lock = Any()

        // invoke metodu ile instance oluşturuyoruz.
        operator fun invoke(context: Context) : CustomSharedPreferences = instance ?: synchronized(lock) {
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        // Burada da değişkenimizi oluşturuyoruz ve yukarıda bu metodu çağırıyor ve atama yapıyoruz.
        private fun makeCustomSharedPreferences(context: Context) : CustomSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()
        }

    }

    // Burada sharedPreferences'e ulaşıyoruz ve baştan direk commit'i true yapıyoruz ve parantezdeki her koyduğumuzu otomatik commit yapıcak.
    fun saveTime(time: Long) {
        sharedPreferences?.edit(commit = true) {
            putLong(PREFERENCES_TIME, time)
        }
    }

    // Kaydettiğimiz zamanı çekmek için metod tanımladık.
    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME, 0)

}