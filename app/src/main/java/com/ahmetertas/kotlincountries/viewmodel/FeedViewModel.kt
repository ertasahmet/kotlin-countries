package com.ahmetertas.kotlincountries.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetertas.kotlincountries.model.Country
import com.ahmetertas.kotlincountries.service.CountryAPIService
import com.ahmetertas.kotlincountries.service.CountryDatabase
import com.ahmetertas.kotlincountries.util.CustomSharedPreferences
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

// Artık viewModel'lerimiz BaseViewModel'den miras alıyor ve coroutine kullanmak için viewModelScope oluşturmaya gerek yok. Direk launch diyerek coroutine scope açabiliyoruz.
class FeedViewModel (application: Application) : BaseViewModel(application) {

    // Api Service'den ve Disposable'dan nesne oluşturuyoruz.
    private val countryApiService = CountryAPIService()
    private val disposable = CompositeDisposable()
    private var customPreferences = CustomSharedPreferences(getApplication())

    // Burada 10 dakikayı nano time'a çeviriyoruz.
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoading = MutableLiveData<Boolean>()


    fun refreshData(){
        val updateTime = customPreferences.getTime()

        // Burada updateTime'ı kontrol ediyoruz, eğer 10 dk'dan az geçtiyse localdeki room db'den, 10 dk'dan fazla geçtiyse api'den verileri çekiyoruz.
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromRoomDb()
        } else {
            getDataFromAPI()
        }
    }

    fun refreshFromAPI(){
        getDataFromAPI()
    }

    private fun getDataFromRoomDb() {
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(), "Countries FROM ROOM DB", Toast.LENGTH_LONG).show()
        }
    }

    private fun getDataFromAPI() {

        countryLoading.value = true

        // Burada disposable sınıfından oluşturduğumuz nesneye add diyoruz ve bunun içinde işlemlerimizi yapacağız.
        disposable.add(

            // Api'ye bağlanıp istek atıyoruz.
            countryApiService.getData()

                // Api'ye isteği yeni bir thread'de at diyoruz.
                .subscribeOn(Schedulers.newThread())

                // Bu data'ya main yani ana thread'de eriş diyoruz
                .observeOn(AndroidSchedulers.mainThread())

                // DisposableSingleObserver tipi ile yeni thread'de bu isteği oluştur diyoruz.
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {

                    // Burada da success ve error durumu geliyor. Ona göre işlemlerimizi yapıyoruz.
                    override fun onSuccess(t: List<Country>) {

                        // Burada gelen listeyi db'ye atıyoruz.
                        storeInRoomDb(t)
                        Toast.makeText(getApplication(), "Countries FROM API", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(e: Throwable) {
                        countryError.value = true
                        countryLoading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun showCountries(countryList: List<Country>) {
        countries.value = countryList
        countryError.value = false
        countryLoading.value = false
    }

    // Api'den liste geldiğinde bu metodu çalıştırıyoruz.
    private fun storeInRoomDb(list: List<Country>) {

        // Launch ile CoroutineScope'umuzu açarak farklı thread'de arkaplanda işlem yapıyoruz.
        launch {

            // Database'imiz üzerinden countryDao'ya ulaşıyoruz.
            val dao = CountryDatabase(getApplication()).countryDao()

            // Önce tablonun içindekileri temizliyoruz.
            dao.deleteAllCountries()

            // Tüm listeyi insertAll ile ekliyoruz. Parametresi valarg olduğu için elimizdeki listeyi başına * koyup toTypedArray diyerek her elemanı teker teker veriyoruz ve foreach dönmemize gerek kalmıyor. Insert işleminden dönen UID'leri alıyoruz.
            val uIdList = dao.insertAll(*list.toTypedArray())
            var i= 0

            // While ile bizim listeyi ve gelen uid listesini dönüyoruz.
            while (i < list.size) {

                // Gelen uid'leri bizim apiden gelen listenin id tarafına ekliyoruz.
                list[i].uId = uIdList[i].toInt()
                i += 1
            }

            // Sonra showCountries metodunu çağırıyoruz. Bu metot ise MutableLiveData nesnelerine listeyi ve diğer şeyleri atama yapıyor ve fragment tarafında da observe olan yerlerin haberi oluyor. Böylece veriyi ekrana basıyoruz.
            showCountries(list)
        }

        // Burada şuanki zamanı nanoTime olarak alıyoruz ve shared Preferences'a kaydediyoruz.
        customPreferences.saveTime(System.nanoTime())
    }

    // Burada disposable'daki nesneleri sıfırladık, hafızayı temizledik.
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}