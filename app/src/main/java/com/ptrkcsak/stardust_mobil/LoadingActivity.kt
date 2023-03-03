package com.ptrkcsak.stardust_mobil

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View

class LoadingActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 5000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        splashScreenAnimation()
    }

    fun splashScreenAnimation() {
        Handler().postDelayed({
                val intent =
                        Intent(this@LoadingActivity, LoginActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }, SPLASH_TIME_OUT.toLong())
    }
}