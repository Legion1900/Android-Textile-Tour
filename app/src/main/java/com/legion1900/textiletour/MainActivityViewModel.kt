package com.legion1900.textiletour

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.textile.pb.Model
import io.textile.pb.QueryOuterClass
import io.textile.textile.Account
import io.textile.textile.BaseTextileEventListener
import io.textile.textile.Textile
import java.util.*

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val eventListener = MyLoggingEventListener()

    private val recoveryPhrase: String

    private val prefs =
        getApplication<Application>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        recoveryPhrase = initTextile()
        Log.d("test", "Phrase: $recoveryPhrase")
    }

    private val isNodeOnline = MutableLiveData<Boolean>()

    private val peerProfile = MutableLiveData<Model.Peer>()

    override fun onCleared() {
        super.onCleared()
        Textile.instance().destroy()
    }

    fun isNodeOnline(): LiveData<Boolean> = isNodeOnline

    /*
    * id - embedded IPFS node`s peer ID (unique on the network);
    * address - wallet account`s address (public key of account), can be shared with other peers;
    * */
    fun getPeerProfile(): LiveData<Model.Peer> = peerProfile

    fun getAccount(): Account = Textile.instance().account

    fun searchContactByName(name: String) {
        val options = QueryOuterClass.QueryOptions.newBuilder()
            .setWait(10)
            .setLimit(1)
            .build()
        val query = QueryOuterClass.ContactQuery.newBuilder().setName(name).build()
        val handler = Textile.instance().contacts.search(query, options)
        Log.d("test", "search by name id: ${handler.id}")
    }

    fun searchContactByAddress(address: String) {
        val options = QueryOuterClass.QueryOptions.newBuilder().setWait(10).setLimit(1).build()
        val query = QueryOuterClass.ContactQuery.newBuilder().setAddress(address).build()
        Textile.instance().contacts.search(query, options)
    }

    private fun initTextile(): String {
        val isDebugLogLvl = false
        val isWriteToDisk = false
        val phrase: String? = Textile.initialize(getApplication(), isDebugLogLvl, isWriteToDisk)
        Textile.instance().apply {
            addEventListener(eventListener)
            addEventListener(object : BaseTextileEventListener() {
                override fun nodeOnline() {
                    // Display name: 'username' in network for communication simplicity.
                    Textile.instance().profile.setName("Tyler ${Calendar.getInstance().time}")
                    // CAREFUL! CALLBACKS ARE CALLED ON THREAD DIFFERENT FROM MAIN!!
                    isNodeOnline.postValue(true)
                    peerProfile.postValue(Textile.instance().profile.get())
                }
            })
        }
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
