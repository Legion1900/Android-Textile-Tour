package com.legion1900.textiletour.view.accountdetails

import android.util.Log
import com.legion1900.textiletour.view.base.BaseViewModel
import io.textile.pb.Model
import io.textile.pb.QueryOuterClass
import io.textile.textile.Account
import io.textile.textile.Textile
import java.util.*

class AccountDetailsViewModel : BaseViewModel() {
    /*
    * Should only be called when node is online!!
    *
    * id - embedded IPFS node`s peer ID (unique on the network);
    * address - wallet account`s address (public key of account), can be shared with other peers;
    * */
    fun getPeerProfile(): Model.Peer = Textile.instance().profile.get()

    fun getAccount(): Account = Textile.instance().account

    fun setName() {
        textile.profile.setName("Tyler ${Calendar.getInstance().time}")
    }
}
