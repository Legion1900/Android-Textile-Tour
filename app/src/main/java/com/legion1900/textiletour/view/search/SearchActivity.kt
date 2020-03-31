package com.legion1900.textiletour.view.search

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.legion1900.textiletour.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

//    TODO: finish later
    private val viewModel by lazy { ViewModelProviders.of(this)[SearchViewModel::class.java] }

    private val SEARCH_BY_NAME by lazy { getString(R.string.search_by_name) }
    private val SEARCH_BY_ADDRESS by lazy { getString(R.string.search_by_address) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        observeViewModel()
        initSpinner()
    }

    private fun observeViewModel() {
        viewModel.searchBy.observe(this, Observer {
            when (it) {
                SEARCH_BY_NAME -> {
                    search_by_name_view.visibility = View.VISIBLE
                    search_by_address_view.visibility = View.GONE
                }
                SEARCH_BY_ADDRESS -> {
                    search_by_name_view.visibility = View.GONE
                    search_by_address_view.visibility = View.VISIBLE
                }
            }
        })

        viewModel.searchResult.observe(this, Observer {

        })
    }

    private fun initSpinner() {
        search_by_spinner.adapter = ArrayAdapter.createFromResource(
            this,
            R.array.search_by,
            android.R.layout.simple_spinner_item
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        search_by_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { /* Nothing to do here */ }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.searchBy.value = search_by_spinner.adapter.getItem(position) as String
            }
        }
    }
}
