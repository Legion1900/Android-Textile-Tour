package com.legion1900.textiletour.view.threads

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.legion1900.textiletour.R
import kotlinx.android.synthetic.main.activity_thread.*
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream

class ThreadActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[ThreadViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread)
        loadThreads()
    }

    fun onAddFileClick(v: View) {
        val file = File.createTempFile("test", "txt")
        PrintStream(FileOutputStream(file)).use {
            it.print("hello")
        }
        viewModel.addFile(file.toString())
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
