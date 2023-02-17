package com.ptrkcsak.stardust_mobil

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import com.google.android.material.bottomappbar.BottomAppBar


class MainActivity : AppCompatActivity() {
    val SPLASH_TIME_OUT = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout_root = findViewById<View>(R.id.layout_root_main) as View
        val animatedDrawable = layout_root.background as AnimationDrawable

        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(5000)
        animatedDrawable.start()

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
        }

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    // Handle search icon press
                    true
                }
                R.id.settings -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

    }


}