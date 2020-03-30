package com.legion1900.textiletour.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.textile.textile.BaseTextileEventListener
import io.textile.textile.Textile
import java.util.*

abstract class BaseViewModel : ViewModel() {
    protected val textile: Textile = Textile.instance()

    protected val isNodeOnline = MutableLiveData<Boolean>().apply { value = false }

    private val nodeStateListener = object : BaseTextileEventListener() {
        override fun nodeOnline() {
            // Display name: 'username' in network for communication simplicity.
            textile.profile.setName("Tyler ${Calendar.getInstance().time}")
            // CAREFUL! CALLBACKS ARE CALLED ON THREAD DIFFERENT FROM MAIN!!
            isNodeOnline.postValue(true)
        }

        override fun nodeStopped() {
            isNodeOnline.postValue(false)
        }
    }

    init {
        textile.addEventListener(nodeStateListener)
    }

    override fun onCleared() {
        super.onCleared()
        textile.removeEventListener(nodeStateListener)
    }

    fun isNodeOnline(): LiveData<Boolean> = isNodeOnline
}
