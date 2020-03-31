package com.legion1900.textiletour.view.threads

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.legion1900.textiletour.R
import kotlinx.android.synthetic.main.activity_thread.*
import kotlin.random.Random

class ThreadActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[ThreadViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        viewModel.getJsonFiles().observe(this, Observer {
            Log.d("test", "Json files: $it")
        })
        viewModel.updateFiles()
    }

    fun onAddJsonThreadClick(v: View) {
        if (viewModel.isNodeOnline.value == true) {
            viewModel.addJsonThread()
            displayThread(ThreadViewModel.NAME_JSON_THREAD)
        } else Toast.makeText(this, "Node if offline", Toast.LENGTH_SHORT).show()
    }

    fun onAddMsgThreadClick(v: View) {
        if (viewModel.isNodeOnline.value == true) {
            viewModel.addMessageThread()
            displayThread(ThreadViewModel.NAME_MSG_THREAD)
        } else Toast.makeText(this, "Node if offline", Toast.LENGTH_SHORT).show()
    }

    fun onAddMessageClick(v: View) {
        viewModel.addMessage(message_view.text.toString())
        Log.d("test", viewModel.getMessages().toString())
    }

    fun onAddJsonFileClick(v: View) {
        val lat = Random.nextFloat() * 10
        val lng = Random.nextFloat() * 10
        Log.d("test", "Generated lat: $lat, lng: $lng")
        viewModel.addJson(lat, lng)
    }

    fun onLogContentClick(v: View) {
        val content = viewModel.getFilesContent().toString()
        Log.d("test", "Content of files: $content")
    }

    private fun displayThread(name: String) {
        thread_view.text = viewModel.getThreadByName(name).toString()
    }
}
