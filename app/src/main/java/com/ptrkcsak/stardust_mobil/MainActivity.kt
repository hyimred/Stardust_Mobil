package com.ptrkcsak.stardust_mobil

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout_root = findViewById<View>(R.id.layout_root) as View
        val animatedDrawable = layout_root.background as AnimationDrawable
        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(3500)
        animatedDrawable.start()
    }
}
