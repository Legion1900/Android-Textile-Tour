package com.legion1900.textiletour.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.legion1900.textiletour.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[MainActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isNodeOnline().observe(this@MainActivity, Observer {
            if (it) {
                peer_profile_view.text = it.toString()
                val seed = viewModel.getAccount().seed()
                val contact = viewModel.getAccount().contact()
                account_view.text = contact.toString()
                seed_view.text = getString(R.string.seed, seed)
                search_btn.isEnabled = true
            }
        })
    }

    fun onSearchClick(v: View) {
        viewModel.searchContactByName("Joe")
    }
}
