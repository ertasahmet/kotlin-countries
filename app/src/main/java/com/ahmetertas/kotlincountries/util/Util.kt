package com.ahmetertas.kotlincountries.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.ahmetertas.kotlincountries.R
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.request.RequestOptions


// Burada bir extension method yazdık. Burada amaç bazı sınıflar için sürekli kullandığımız şeyler varsa bu şekilde method tanımlayıp çok basit bir şekilde işlemlerimizi uygulamayabiliriz. Burada ImageView sınıfına downloadFromUrl adında extension method oluşturduk. Ve  artık her bir image nesnesinden .downloadFromUrl() deyip erişebileceğiz.
fun ImageView.downloadFromUrl(url: String?, progressDrawable: CircularProgressDrawable){

    // Burada Glide kütüphanesi ile internetten fotoğraf çekiyoruz. Fotoğraf yüklenirken de aşağıda tanımladığımız metodda bir progressBar oluşturuyoruz ve onu fotoğraf yüklenene kadar gösteriyoruz.
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    // Burada da yukarıda oluşturduğumuz ayarları ekleyip fotoğrafı çekiyoruz.
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

// Burada da bir progressBar oluşturduk. Apply metodu ile istediğimiz bir nesnenin tüm özelliklerini bu metodun içinde atayabiliriz. yani nesne.strokeWidth yerine apply içinde direk strokeWidth diyebiliyoruz.
fun placeholderProgressBar(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 48f
        start()
    }
}

// Burada bir metod tanımladık ve amacımız bu metodun data Binding ile xml tarafında çağırılması. Bu sayede fotoğrafı gösterelim diyoruz. Başına BindingAdapter annotation'ı koyup bir isim verince xml tarafında görünür olacak.
@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url: String?){
    view.downloadFromUrl(url, placeholderProgressBar(view.context))
}