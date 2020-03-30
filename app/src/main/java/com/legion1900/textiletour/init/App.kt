package com.legion1900.textiletour.init

import android.app.Application
import android.content.Context
import com.legion1900.textiletour.listeners.MyLoggingEventListener
import io.textile.textile.Textile

class App : Application() {
    private val logListener = MyLoggingEventListener()

    private val prefs by lazy { getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    lateinit var recoveryPhrase: String
        private set

    override fun onCreate() {
        super.onCreate()
        recoveryPhrase = initTextile()
    }

    private fun initTextile(): String {
        val isDebugLogLvl = false
        val isWriteToDisk = false
        val phrase: String? = Textile.initialize(this, isDebugLogLvl, isWriteToDisk)
        Textile.instance().addEventListener(logListener)
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
        val KEY_PHRASE = "${App::class.java.simpleName}.RECOVERY_PHRASE"
    }
}
