package com.hamidrezaAmz.magicalbottomnavigation.presenter.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.hamidrezaAmz.magicalbottomnavigation.R
import com.hamidrezaamz.magicalbottomnavigationview.MagicalBNV

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    lateinit var selectedItem: AppCompatImageView

    lateinit var showIconSwitch: SwitchCompat

    lateinit var magicalBNV: MagicalBNV

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        selectedItem = findViewById(R.id.appCompatImageView_selected_item)

        showIconSwitch = findViewById(R.id.switchCompat_show_icon)
        showIconSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

            } else {

            }
        }

        magicalBNV = findViewById(R.id.magicalBottomNavigationView)
        magicalBNV.setOnItemReselectedListener {
            // ...
        }
        magicalBNV.selectedItemId = R.id.action_more
    }

}