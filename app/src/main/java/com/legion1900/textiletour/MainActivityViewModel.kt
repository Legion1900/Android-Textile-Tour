package com.legion1900.textiletour

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.textile.pb.Model
import io.textile.textile.BaseTextileEventListener
import io.textile.textile.Textile

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val recoveryPhrase: String

    private val prefs =
        getApplication<Application>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        recoveryPhrase = initTextile()
        Log.d("test", "Phrase: $recoveryPhrase")
    }

    private val peerProfile = MutableLiveData<Model.Peer>()

    fun getPeerProfile(): LiveData<Model.Peer> = peerProfile

    private fun initTextile(): String {
        val isDebugLogLvl = true
        val isWriteToDisk = false
        val phrase: String? = Textile.initialize(getApplication(), isDebugLogLvl, isWriteToDisk)
        Textile.instance().addEventListener(object : BaseTextileEventListener() {
            override fun nodeOnline() {
                Log.d("test", "nodeOnline on thread: ${Thread.currentThread()}")
                // TODO: CAREFUL! CALLBACKS ARE CALLED ON THREAD DIFFERENT FROM MAIN!!
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