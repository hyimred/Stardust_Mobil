package com.ptrkcsak.stardust_mobil

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val layout_root = findViewById<View>(R.id.layout_root_profile) as View
        val animatedDrawable = layout_root.background as AnimationDrawable

        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(5000)
        animatedDrawable.start()
    }
}