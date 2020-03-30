package com.legion1900.textiletour.view

import android.util.Log
import com.legion1900.textiletour.view.base.BaseViewModel
import io.textile.pb.Model
import io.textile.pb.QueryOuterClass
import io.textile.textile.Account
import io.textile.textile.Textile

class MainActivityViewModel : BaseViewModel() {

    /*
    * Should only be called when node is online!!
    *
    * id - embedded IPFS node`s peer ID (unique on the network);
    * address - wallet account`s address (public key of account), can be shared with other peers;
    * */
    fun getPeerProfile(): Model.Peer = Textile.instance().profile.get()

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
}
