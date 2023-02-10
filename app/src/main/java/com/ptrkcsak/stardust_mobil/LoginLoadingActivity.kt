package com.ptrkcsak.stardust_mobil

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast

class LoginLoadingActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 5000;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_loading)

        val layout_root = findViewById<View>(R.id.layout_root_loading) as View
        val animatedDrawable = layout_root.background as AnimationDrawable
        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(5000)
        animatedDrawable.start()
        splashScreenAnimation()
    }

    fun splashScreenAnimation() {
        Handler().postDelayed({
                val intent =
                        Intent(this@LoginLoadingActivity, MainActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }, SPLASH_TIME_OUT.toLong())
    }
}