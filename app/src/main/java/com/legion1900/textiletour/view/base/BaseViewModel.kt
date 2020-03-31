package com.legion1900.textiletour.view.base

import androidx.lifecycle.ViewModel
import com.legion1900.textiletour.init.App
import io.textile.textile.Textile

abstract class BaseViewModel : ViewModel() {
    protected val textile: Textile = Textile.instance()

    val isNodeOnline = App.isNodeOnline()
}
