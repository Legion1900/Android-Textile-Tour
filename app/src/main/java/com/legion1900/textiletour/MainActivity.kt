package com.legion1900.textiletour

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[MainActivityViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("test", "onCreate on thread: ${Thread.currentThread()}")
        viewModel.getPeerProfile().observe(this, Observer {
            peer_profile_view.text = it.toString()
            display_name_view.text = getString(R.string.display_name, it.name)
        })
    }
}
