package com.legion1900.textiletour.view.threads

import android.util.Log
import com.legion1900.textiletour.view.base.BaseViewModel
import io.textile.pb.Model
import io.textile.pb.View

class ThreadViewModel : BaseViewModel() {
    fun getThread(): Model.Thread? {
        return if (textile.threads.list().itemsCount != 0)
            textile.threads.list().getItems(0)
        else null
    }

    fun addThread() {
        textile.threads.add(config)
    }

    fun addMessage(msg: String) {
        val id = getThread()!!.id
        textile.messages.add(id, msg)
    }

    fun getMessages(): View.TextList = textile.messages.list(null, 300, getThread()!!.id)

    private companion object {
        const val THREAD_NAME = "My Thread"
        val KEY = "${ThreadViewModel::class.java.`package`.toString()}.BLOB"
        val SCHEMA: View.AddThreadConfig.Schema = View.AddThreadConfig.Schema.newBuilder()
            .setPreset(View.AddThreadConfig.Schema.Preset.BLOB)
            .build()
        val config: View.AddThreadConfig = View.AddThreadConfig.newBuilder()
            .setKey(KEY)
            .setName(THREAD_NAME)
            .setType(Model.Thread.Type.PRIVATE)
            .setSharing(Model.Thread.Sharing.NOT_SHARED)
            .setSchema(SCHEMA)
            .build()
    }
}