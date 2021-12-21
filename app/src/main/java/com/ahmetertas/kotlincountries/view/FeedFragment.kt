package com.ahmetertas.kotlincountries.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmetertas.kotlincountries.R
import com.ahmetertas.kotlincountries.adapter.CountryAdapter
import com.ahmetertas.kotlincountries.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FeedFragment : Fragment() {

    private lateinit var viewModel : FeedViewModel
    private val countryAdapter = CountryAdapter(arrayListOf())

    private lateinit var root : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_feed, container, false)

        root.rvCountry.layoutManager = LinearLayoutManager(context)
        root.rvCountry.adapter = countryAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // val action = FeedFragmentDirections

        viewModel = ViewModelProviders.of(this).get(FeedViewModel::class.java)
        viewModel.refreshData()

        // Burada swipeRefreshLayout ile verileri yenilemeyi gösterdik. Refresh yapıldığında burası çalışıyor ve viewModel'den tekrar verileri çağırıyoruz.
        swipeRefreshLayout.setOnRefreshListener {
            rvCountry.visibility = View.GONE
            countryError.visibility = View.GONE
            countryLoading.visibility = View.VISIBLE
            viewModel.refreshFromAPI()
            swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    fun observeLiveData() {

        viewModel.countries.observe(viewLifecycleOwner, { countries ->
            countries?.let {
                rvCountry.visibility = View.VISIBLE
                countryAdapter.updateCountryList(countries)
            }
        })

        viewModel.countryError.observe(viewLifecycleOwner, {error ->
            error?.let {
                if(it) {
                    countryError.visibility = View.VISIBLE
                } else {
                    countryError.visibility = View.GONE
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, {
            if (it) {
                countryLoading.visibility = View.VISIBLE
                rvCountry.visibility = View.GONE
                countryError.visibility = View.GONE
            } else {
                countryLoading.visibility = View.GONE
            }
        })
    }

}