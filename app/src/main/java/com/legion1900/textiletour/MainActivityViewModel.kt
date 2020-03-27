package com.legion1900.textiletour

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import io.textile.textile.Textile

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val recoveryPhrase: String

    private val prefs =
        getApplication<Application>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    init {
        recoveryPhrase = initTextile()
        Log.d("test", "Phrase: $recoveryPhrase")

        Log.d("test", "Is node online: ${Textile.instance().online()}")
    }

    fun getPeer() = Textile.instance().profile.get()

    private fun initTextile(): String {
        val isDebugLogLvl = true
        val isWriteToDisk = false
        val phrase: String? = Textile.initialize(getApplication(), isDebugLogLvl, isWriteToDisk)
        Log.d("test", "Textile init phrase: $phrase")

        Textile.instance().addEventListener(MyTextileEventListener())

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