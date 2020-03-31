package com.legion1900.textiletour.view.threads

import android.util.Log
import com.legion1900.textiletour.view.base.BaseViewModel
import io.textile.pb.Model
import io.textile.pb.View
import io.textile.textile.Handlers

class ThreadViewModel : BaseViewModel() {

    init {
//        Log.d("test", getThread()!!.blockCount.toString())
//        Log.d("test", getThread()!!.headBlocksCount.toString())
//        Log.d("test", getThread()!!.headBlocksList.toString())
//        Log.d("test", textile.files.list(getThread()!!.id, "", 1000).allFields.toString())
//        textile.files.list(getThread()!!.id, "", 1000).allFields.entries.forEach {
//            //TODO: how to work with this???
//            Log.d("test", it.value::class.simpleName.toString())
//        }
//        getThread()!!.headBlocksCount
    }

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

    fun addFile(paths: String) {
        val caption = ""
        textile.files.addFiles(paths, getThread()!!.id, caption, object : Handlers.BlockHandler {
            override fun onComplete(block: Model.Block?) {
                Log.d("test", block.toString())
            }

            override fun onError(e: Exception?) {
                Log.d("test", "Error occurred", e)
            }
        })
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