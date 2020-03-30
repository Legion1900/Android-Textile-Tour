package com.legion1900.textiletour

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.textile.pb.Model
import io.textile.pb.QueryOuterClass
import io.textile.textile.BaseTextileEventListener
import io.textile.textile.Textile
import java.util.*

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val recoveryPhrase: String

    private val prefs =
        getApplication<Application>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        recoveryPhrase = initTextile()
        Log.d("test", "Phrase: $recoveryPhrase")
    }

    private val isNodeOnline = MutableLiveData<Boolean>()

    private val peerProfile = MutableLiveData<Model.Peer>()

    fun isNodeOnline(): LiveData<Boolean> = isNodeOnline

    /*
    * id - embedded IPFS node`s peer ID (unique on the network);
    * address - wallet account`s address (public key of account), can be shared with other peers;
    * */
    fun getPeerProfile(): LiveData<Model.Peer> = peerProfile

    fun getAccount() = Textile.instance().account

    fun searchForContact(name: String) {
        val options = QueryOuterClass.QueryOptions.newBuilder()
            .setWait(10)
            .setLimit(1)
            .build()
        val query = QueryOuterClass.ContactQuery.newBuilder().setName(name).build()
        Textile.instance().addEventListener(MyLoggingEventListener())
        val handler = Textile.instance().contacts.search(query, options)
        Log.d("test", "search by name id: ${handler.id}")
    }

    private fun initTextile(): String {
        val isDebugLogLvl = true
        val isWriteToDisk = false
        val phrase: String? = Textile.initialize(getApplication(), isDebugLogLvl, isWriteToDisk)
        Textile.instance().addEventListener(object : BaseTextileEventListener() {
            override fun nodeOnline() {
                Log.d("test", "nodeOnline on thread: ${Thread.currentThread()}")
                // Display name: 'username' in network for communication simplicity.
                Textile.instance().profile.setName("Tyler ${Calendar.getInstance().time}")
                // CAREFUL! CALLBACKS ARE CALLED ON THREAD DIFFERENT FROM MAIN!!
                isNodeOnline.postValue(true)
                peerProfile.postValue(Textile.instance().profile.get())
            }
        })
        return phrase?.apply { savePhrase(this) } ?: loadPhrase()
    }

    private fun savePhrase(phrase: String) {
        with(prefs.edit()) {
            putString(KEY_PHRASE, phrase)
            apply()
        }
    }

    private fun loadPhrase() = prefs.getString(KEY_PHRASE, "")!!

    private companion object {
        const val PREF_NAME = "TEXTILE_DATA"
        val KEY_PHRASE = "${MainActivityViewModel::class.java.simpleName}.RECOVERY_PHRASE"
    }
}
