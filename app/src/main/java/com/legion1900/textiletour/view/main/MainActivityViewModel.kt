package com.legion1900.textiletour.view.main

import com.legion1900.textiletour.view.threads.ThreadActivity
import com.legion1900.textiletour.view.accountdetails.AccountDetailsActivity
import com.legion1900.textiletour.view.base.BaseViewModel
import com.legion1900.textiletour.view.search.SearchActivity

class MainActivityViewModel : BaseViewModel() {
    val screens = linkedMapOf(
        "Account Details" to AccountDetailsActivity::class.java,
        "Search Users" to SearchActivity::class.java,
        "Threads" to ThreadActivity::class.java
    )
}
