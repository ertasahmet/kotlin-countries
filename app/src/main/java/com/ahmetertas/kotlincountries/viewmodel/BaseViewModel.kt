package com.ahmetertas.kotlincountries.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    // Burada da bir job oluşturduk. Bizim yapacağımız işlemi temsil ediyor.
    private val job = Job()

    // Burada diyoruz ki coroutine context olarak normal job'ı çalıştır yani yapacağımız işi. Daha sonra main Thread'de işlemine devam et.
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    // İşlem bittiğinde yani cleared olduğunda job'ı da clear et diyoruz.
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}