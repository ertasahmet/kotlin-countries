package com.ahmetertas.kotlincountries.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.ahmetertas.kotlincountries.R
import com.ahmetertas.kotlincountries.databinding.ItemCountryBinding
import com.ahmetertas.kotlincountries.model.Country
import com.ahmetertas.kotlincountries.util.downloadFromUrl
import com.ahmetertas.kotlincountries.util.placeholderProgressBar
import com.ahmetertas.kotlincountries.view.FeedFragmentDirections
import kotlinx.android.synthetic.main.item_country.view.*

// Burada CountryClickListener'i da implemente ettik ki click olayını halledelim.
class CountryAdapter (val countryList : ArrayList<Country>) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), CountryClickListener {

/*
    class CountryViewHolder (var view: View) : RecyclerView.ViewHolder(view) {

    }*/

    // Burada view class'ından değil de resource olarak hangi dosyamızda data etiketlerini kullandıysak onun sınıf ismiyle çağırıyoruz. Mesela layout dosyamızın adı item_country, burada çağırdığımız class ItemCountryBinding. Bu şekilde yapıyoruz. Miras aldığımız sınıfa da view.root diyerek view'ı gönderiyoruz.
    class CountryViewHolder (var view: ItemCountryBinding) : RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
       // val view = inflater.inflate(R.layout.item_country, parent, false)

        // Burada da tasarımı yüklemeyi normal inflater ile değil de DataBindingUtil sınıfı ile inflate ediyoruz ve tipini yine ItemCountryBinding olarak verip tasarımı belirtiyoruz.
        val view = DataBindingUtil.inflate<ItemCountryBinding>(inflater, R.layout.item_country, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        var country = countryList[position]

        //  Burada da layout tasarıfında tanımladığımız country değişkenine verimizi atıyoruz, bu sayede ekrana basıyor.
        holder.view.country = country

        // Burada da xml tarafında oluşturduğumuz interface'i bu class'ın karşılayacağını söylüyoruz.
        holder.view.listener = this

     /*   holder.view.findViewById<TextView>(R.id.nameCountry).text = country.countryName
        holder.view.findViewById<TextView>(R.id.nameRegion).text = country.countryRegion

        // Burada da oluşturduğumuz extension metodu kullandık ve fotoğrafı aldık.
        holder.view.imgCountry.downloadFromUrl(country.countryImage, placeholderProgressBar(holder.view.context))

        holder.view.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(country.uId)
            Navigation.findNavController(it).navigate(action)
        }*/
    }

    override fun getItemCount(): Int {
        return countryList.size
    }


    fun updateCountryList(newCountryList : List<Country>) {
        countryList.clear()
        countryList.addAll(newCountryList)
        notifyDataSetChanged()
    }

    override fun onCountryClicked(v: View) {
        val uid = v.countryUId.text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uid)
        Navigation.findNavController(v).navigate(action)
    }
}