package com.ptrkcsak.stardust_mobil

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val layout_root = findViewById<View>(R.id.layout_root_login) as View
        val animatedDrawable = layout_root.background as AnimationDrawable
        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(5000)
        animatedDrawable.start()

        val btn_register = findViewById<View>(R.id.btn_register) as Button
        btn_register.setOnClickListener {
            Handler().postDelayed({
                val intent =
                    Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }, SPLASH_TIME_OUT.toLong())
        }
    }
}
