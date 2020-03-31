package com.legion1900.textiletour.view.threads

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.legion1900.textiletour.view.base.BaseViewModel
import io.textile.pb.Mobile
import io.textile.pb.Model
import io.textile.pb.View
import io.textile.textile.Files


class ThreadViewModel : BaseViewModel() {

    private val jsonFiles = MutableLiveData<List<View.Files>>()

    fun getJsonFiles(): LiveData<List<View.Files>> = jsonFiles

    fun getThreadByName(name: String): Model.Thread? {
        val threads = textile.threads.list().itemsList
        return if (threads.size != 0) {
            threads.filter { it.name == name }[0]
        } else null
    }

    fun addMessageThread() {
        /*
        * Will throw go.Universe$proxyerror: UNIQUE constraint failed: threads.key on same key used
        * twice.
        * */
        val schema: View.AddThreadConfig.Schema = View.AddThreadConfig.Schema.newBuilder()
            .setPreset(View.AddThreadConfig.Schema.Preset.BLOB)
            .build()
        val config: View.AddThreadConfig = View.AddThreadConfig.newBuilder()
            .setKey(KEY_MSG_THREAD)
            .setName(NAME_MSG_THREAD)
            .setType(Model.Thread.Type.PRIVATE)
            .setSharing(Model.Thread.Sharing.NOT_SHARED)
            .setSchema(schema)
            .build()
        textile.threads.add(config)
    }

    fun addJsonThread() {
        val schema = View.AddThreadConfig.Schema.newBuilder().setJson(JSON_SCHEMA).build()
        val config = View.AddThreadConfig.newBuilder()
            .setKey(KEY_JSON_THREAD)
            .setName(NAME_JSON_THREAD)
            .setType(Model.Thread.Type.PRIVATE)
            .setSharing(Model.Thread.Sharing.NOT_SHARED)
            .setSchema(schema)
            .build()
        textile.threads.add(config)
    }

    fun removeThreadByName(name: String) {
        val threadId = textile.threads.list().itemsList.filter { it.name == name }[0].id
        textile.threads.remove(threadId)
    }

    fun addMessage(msg: String) {
        val id = getThreadByName(NAME_MSG_THREAD)!!.id
        textile.messages.add(id, msg)
    }

    fun addJson(lat: Float, lng: Float) {
        val threadId = getThreadByName(NAME_JSON_THREAD)!!.id
        val jsonData = "{ \"latitude\": $lat, \"longitude\": $lng }".toByteArray()
        val dataString = Base64.encodeToString(jsonData, Base64.DEFAULT)
        textile.files.prepare(dataString, threadId, object : Files.PreparedFilesHandler {
            override fun onFilesPrepared(preparedFiles: Mobile.MobilePreparedFiles?) {
                textile.files.add(preparedFiles?.dir, threadId, null)
                updateFiles()
            }

            override fun onError(e: Exception?) {
                Log.e("test", "Error occurred on file addition", e)
                Log.e("test", "jsonData bytes: ${jsonData.contentToString()}")
                Log.e("test", "dataString: $dataString")
            }
        })
    }

    fun getMessages(): View.TextList {
        return textile.messages.list(null, 300, getThreadByName(NAME_MSG_THREAD)!!.id)
    }

    fun getFilesContent(): List<String> {
        val threadId = getThreadByName(NAME_JSON_THREAD)!!.id
        /*
        * textile.files.list(null, Long.MAX_VALUE, threadId): FilesList
        * textile.files.list(null, Long.MAX_VALUE, threadId).itemsList: List<Files>
        * ...[i].file: File - exactly what we need!!
        * */
        return textile.files.list(null, Long.MAX_VALUE, threadId).itemsList.map {
            textile.files.data(it.getFiles(0).file.hash).decode()
        }
    }

    private fun String.decode(): String {
        /*
        * Data comes in form of such string:
        * data:application/json;base64,eyJsYXRpdHVkZSI6Ny44NjcyMzM4LCJsb25naXR1ZGUiOjkuNjExNjc1fQ== ;
        * encoded data must be separated.
        * */
        val encoded = split(',').last()
        val bytes = Base64.decode(encoded, Base64.DEFAULT)
        return String(bytes)
    }

    fun updateFiles() {
        val files = textile.files.list(null, 1000, getThreadByName(NAME_JSON_THREAD)!!.id).itemsList
        jsonFiles.postValue(files)
    }

    companion object {
        // Thread key must be unique for every thread!
        const val NAME_MSG_THREAD = "MessageThread"
        const val NAME_JSON_THREAD = "JsonThread"
        private val KEY_MSG_THREAD = "${ThreadViewModel::class.java.`package`.toString()}.BLOB_MSG"
        private val KEY_JSON_THREAD =
            "${ThreadViewModel::class.java.`package`.toString()}.JSON_FILE"
        private val JSON_SCHEMA = "{\n" +
                "    \"name\": \"location\",\n" +
                "    \"mill\": \"/json\",\n" +
                "    \"json_schema\": {\n" +
                "        \"\$id\": \"https://example.com/geographical-location.schema.json\",\n" +
                "        \"\$schema\": \"http://json-schema.org/draft-07/schema#\",\n" +
                "        \"title\": \"Longitude and Latitude Values\",\n" +
                "        \"description\": \"A geographical coordinate.\",\n" +
                "        \"required\": [ \"latitude\", \"longitude\" ],\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "            \"latitude\": {\n" +
                "                \"type\": \"number\",\n" +
                "                \"minimum\": -90,\n" +
                "                \"maximum\": 90\n" +
                "            },\n" +
                "            \"longitude\": {\n" +
                "                \"type\": \"number\",\n" +
                "                \"minimum\": -180,\n" +
                "                \"maximum\": 180\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n"
    }
}