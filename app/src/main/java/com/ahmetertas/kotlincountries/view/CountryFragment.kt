package com.ahmetertas.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ahmetertas.kotlincountries.R
import com.ahmetertas.kotlincountries.databinding.FragmentCountryBinding
import com.ahmetertas.kotlincountries.util.downloadFromUrl
import com.ahmetertas.kotlincountries.util.placeholderProgressBar
import com.ahmetertas.kotlincountries.viewmodel.CountryViewModel
import com.ahmetertas.kotlincountries.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_country.*

class CountryFragment : Fragment() {

    private lateinit var viewModel : CountryViewModel
    private var countryUId = 0

    // Fragment'ta data binding uygulamak için yine ilgili layout dosyası adı + binding ismi ile sınıf oluşturuluyor. burada da layout dosyası fragment_country. Sınıfımızın ismi de FragmentCountryBinding'tir. Burdan bir binding nesnesi tanımladık.
    private lateinit var dataBinding : FragmentCountryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Layout'umuzu buradan çağırdık.
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_country, container, false)

        // Ve geriye binding'in içindeki root'u döndük.
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        arguments?.let { 
            countryUId = CountryFragmentArgs.fromBundle(it).countryUId
        }

        viewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        viewModel.getDataFromRoom(countryUId)

        observeLiveData()

    }

    // Observe işleminde ise sadece xml tarafında modelimize atama yapıyoruz, gerisini o hallediyor.
    private fun observeLiveData() {
        viewModel.countryLiveData.observe(viewLifecycleOwner, { country ->
            country?.let {

                dataBinding.selectedCountry = it

              /*  countryName.text = country.countryName
                countryCapital.text = country.countryCapital
                countryCurrency.text = country.countryCurrency
                countryLanguage.text = country.countryLanguage
                countryRegion.text = country.countryRegion

                context?.let {
                    imgCountryDetail.downloadFromUrl(country.countryImage, placeholderProgressBar(it))
                }*/
            }
        })
    }

}