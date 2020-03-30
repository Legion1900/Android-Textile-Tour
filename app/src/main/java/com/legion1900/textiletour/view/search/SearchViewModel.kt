package com.legion1900.textiletour.view.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.legion1900.textiletour.view.base.BaseViewModel
import io.textile.pb.Model
import io.textile.pb.QueryOuterClass
import io.textile.textile.BaseTextileEventListener
import io.textile.textile.Textile

class SearchViewModel : BaseViewModel() {

    val searchBy = MutableLiveData<String>()

    val searchResult = MutableLiveData<List<Model.Contact>>()

    private val results = mutableListOf<Model.Contact>()

    private val resultListener = object : BaseTextileEventListener() {
        override fun contactQueryResult(queryId: String?, contact: Model.Contact?) {
            results += contact!!
        }

        override fun queryDone(queryId: String?) {
            searchResult.postValue(results)
        }
    }

    init {
        textile.addEventListener(resultListener)
    }

    override fun onCleared() {
        super.onCleared()
        textile.removeEventListener(resultListener)
    }

    fun searchContactByAddress(address: String) {
        results.clear()
        val options = QueryOuterClass.QueryOptions.newBuilder().setWait(10).setLimit(1).build()
        val query = QueryOuterClass.ContactQuery.newBuilder().setAddress(address).build()
        Textile.instance().contacts.search(query, options)
    }

    fun searchContactByName(name: String) {
        results.clear()
        val options = QueryOuterClass.QueryOptions.newBuilder()
            .setWait(10)
            .setLimit(1)
            .build()
        val query = QueryOuterClass.ContactQuery.newBuilder().setName(name).build()
        val handler = Textile.instance().contacts.search(query, options)
        Log.d("test", "search by name id: ${handler.id}")
    }
}