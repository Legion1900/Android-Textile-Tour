package com.legion1900.textiletour.view.search

import android.util.Log
import androidx.lifecycle.ViewModel
import io.textile.pb.QueryOuterClass
import io.textile.textile.Textile

class SearchViewModel : ViewModel() {
    fun searchContactByAddress(address: String) {
        val options = QueryOuterClass.QueryOptions.newBuilder().setWait(10).setLimit(1).build()
        val query = QueryOuterClass.ContactQuery.newBuilder().setAddress(address).build()
        Textile.instance().contacts.search(query, options)
    }

    fun searchContactByName(name: String) {
        val options = QueryOuterClass.QueryOptions.newBuilder()
            .setWait(10)
            .setLimit(1)
            .build()
        val query = QueryOuterClass.ContactQuery.newBuilder().setName(name).build()
        val handler = Textile.instance().contacts.search(query, options)
        Log.d("test", "search by name id: ${handler.id}")
    }
}