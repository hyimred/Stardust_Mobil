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

        val btn_submit = findViewById<View>(R.id.btn_submit) as Button
        btn_submit.setOnClickListener {
            Handler().postDelayed({
                val intent =
                    Intent(this@LoginActivity, LoginLoadingActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }, SPLASH_TIME_OUT.toLong())
        }
    }
}
