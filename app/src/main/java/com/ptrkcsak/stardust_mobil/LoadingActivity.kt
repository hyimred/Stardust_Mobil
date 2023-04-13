package com.ptrkcsak.stardust_mobil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


class LoadingActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 5000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        splashScreenAnimation()
    }

    fun splashScreenAnimation() {
        val prefs = getSharedPreferences("Important", Context.MODE_PRIVATE)
        val token = prefs.getString("access_token", null)
        Handler().postDelayed({
            if (token == "" || token == null) {
                val intent =
                    Intent(this@LoadingActivity, LoginActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                val intent =
                    Intent(this@LoadingActivity, MainActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                Log.d("USERTOKEN", token)
            }
        }, SPLASH_TIME_OUT.toLong())
    }
}