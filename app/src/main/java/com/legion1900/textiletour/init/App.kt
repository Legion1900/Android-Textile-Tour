package com.legion1900.textiletour.init

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.legion1900.textiletour.listeners.MyLoggingEventListener
import io.textile.textile.BaseTextileEventListener
import io.textile.textile.Textile
import java.util.*

class App : Application() {
    private val logListener = MyLoggingEventListener()

    private val nodeStateListener = object : BaseTextileEventListener() {
        override fun nodeOnline() {
            // CAREFUL! CALLBACKS ARE CALLED ON THREAD DIFFERENT FROM MAIN!!
            Log.d("test", "node online on thread: ${Thread.currentThread()}")
            isNodeOnline.postValue(true)
        }

        override fun nodeStopped() {
            isNodeOnline.postValue(false)
        }
    }

    private val prefs by lazy { getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    override fun onCreate() {
        super.onCreate()
        recoveryPhrase = initTextile()
    }

    private fun initTextile(): String {
        val isDebugLogLvl = false
        val isWriteToDisk = false
        val phrase: String? = Textile.initialize(this, isDebugLogLvl, isWriteToDisk)
        Textile.instance().addEventListener(nodeStateListener)
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

    companion object {
        private const val PREF_NAME = "TEXTILE_DATA"

        @JvmStatic
        private val KEY_PHRASE = "${App::class.java.simpleName}.RECOVERY_PHRASE"

        @JvmStatic
        lateinit var recoveryPhrase: String
            private set

        @JvmStatic
        private val isNodeOnline = MutableLiveData<Boolean>()

        @JvmStatic
        fun isNodeOnline(): LiveData<Boolean> = isNodeOnline
    }
}
