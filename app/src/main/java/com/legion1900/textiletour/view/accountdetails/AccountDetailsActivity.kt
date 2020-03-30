package com.legion1900.textiletour.view.accountdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.legion1900.textiletour.R
import kotlinx.android.synthetic.main.activity_account_details.*

class AccountDetailsActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this)[AccountDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)
        observeViewModel()
        safeDataUpdate()
    }

    private fun observeViewModel() {
        viewModel.isNodeOnline().observe(this, Observer { isOnline ->
            if (isOnline) {
                requestData()
            }
        })
    }

    private fun safeDataUpdate() {
        if (viewModel.isNodeOnline().value == true) {
            requestData()
        }
    }

    private fun requestData() {
        account_info_view.text = viewModel.getAccount().toString()
        profile_info_view.text = viewModel.getPeerProfile().toString()
    }
}
