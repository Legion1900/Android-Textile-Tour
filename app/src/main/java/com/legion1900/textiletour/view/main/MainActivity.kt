package com.legion1900.textiletour.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import com.legion1900.textiletour.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_screen_view.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[MainActivityViewModel::class.java] }

    private val adapter = ListDelegationAdapter(createScreenDelegate())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter.items = viewModel.screens.toList()
        screens_view.layoutManager = LinearLayoutManager(this)
        screens_view.adapter = adapter
    }

    private fun createScreenDelegate() =
        adapterDelegateLayoutContainer<Pair<String, Class<*>>, Pair<String, Class<*>>>(
            R.layout.item_screen_view
        ) {
            itemView.setOnClickListener { startActivity(Intent(this@MainActivity, item.second)) }
            bind {
                screen_name_view.text = item.first
            }
        }
}
