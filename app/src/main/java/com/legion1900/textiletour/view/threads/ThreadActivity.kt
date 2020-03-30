package com.legion1900.textiletour.view.threads

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.legion1900.textiletour.R
import kotlinx.android.synthetic.main.activity_thread.*

class ThreadActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[ThreadViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        loadThreads()
    }

    fun onAddThreadClick(v: View) {
        if (viewModel.isNodeOnline().value == true) {
            viewModel.addThread()
            loadThreads()
        } else Toast.makeText(this, "Node if offline", Toast.LENGTH_SHORT).show()
    }

    fun onAddMessageClick(v: View) {
        viewModel.addMessage(message_view.text.toString())
        Log.d("test", viewModel.getMessages().toString())
    }

    private fun loadThreads() {
        thread_view.text = viewModel.getThread().toString()
    }
}
