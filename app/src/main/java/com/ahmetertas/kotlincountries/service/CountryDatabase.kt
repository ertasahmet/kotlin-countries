package com.ahmetertas.kotlincountries.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmetertas.kotlincountries.model.Country

// Burada da bu sınıfın database sınıfı olduğunu söylüyoruz, entity'leri burada veriyoruz ve versiyon belirtiyoruz. RoomDatabase sınıfından da miras alıyoruz.
@Database(entities = [Country::class], version = 1)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao() : CountryDAO

    companion object {

        // Buradaki volatile sayesinde bu değişkene her thread'den erişebiliyor olacağız.
        @Volatile private var instance : CountryDatabase? = null

        // Öylesine bir nesne tanımladık.
        private var lock = Any()

        // invoke diye bir metod tanımladık bunun amacı singleton uygulayıp instance dönmektir. Instance'ın null olup olmadığını kontrol ediyoruz. Eğer null ise  synchronized diye bir metod açıyoruz. Bu metod gelen istekleri senkron şekilde sırayla çalıştırır ki aynı class'tan birsürü farklı instance üretmeyeyim diye. O yüzden onu kullandık, lock değişkenini verdik, sonra içine girdik.
        operator fun invoke(context : Context) = instance ?: synchronized(lock) {

            // Burada yine instance'ın null olup olmadığına bakıyoruz. Null ise database'i oluştur diyoruz, contexti veriyoruz. ve ayrı şunları da yap diye also açıyoruz. Dönen datayı instance'a atıyoruz.
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        // MakeDatabase diye bir metod oluşturduk ve Room'un databaseBuilder metodunu çağırıyor ve db'yi oluşturuyoruz. Db class'ını, context'i ve db adını veriyoruz ve build ile oluştur diyoruz.
        private fun makeDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext, CountryDatabase::class.java, "countrydatabase"
        ).build()

    }

}