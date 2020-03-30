package com.legion1900.textiletour

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.textile.pb.QueryOuterClass
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[MainActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.apply {
            getPeerProfile().observe(this@MainActivity, Observer {
                peer_profile_view.text = it.toString()
                display_name_view.text = getString(R.string.display_name, it.name)
            })
            isNodeOnline().observe(this@MainActivity, Observer {
                val seed = getAccount().seed()
                val contact = getAccount().contact()
                account_view.text = contact.toString()
                seed_view.text = getString(R.string.seed, seed)
                search_btn.isEnabled = true
            })
        }
    }

    fun onSearchClick(v: View) {
        viewModel.searchForContact("Joe")
    }
}
