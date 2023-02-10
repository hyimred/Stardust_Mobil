package com.ptrkcsak.stardust_mobil

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val layout_root = findViewById<View>(R.id.layout_root_register) as View
        val animatedDrawable = layout_root.background as AnimationDrawable
        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(5000)
        animatedDrawable.start()

        val btn_login = findViewById<View>(R.id.btn_login) as Button
        btn_login.setOnClickListener {
            Handler().postDelayed({
                val intent =
                    Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }, SPLASH_TIME_OUT.toLong())
        }

        val btn_submit = findViewById<View>(R.id.btn_submit) as Button
        btn_submit.setOnClickListener {
            Handler().postDelayed({
                val intent =
                    Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

            }, SPLASH_TIME_OUT.toLong())
            Toast.makeText(applicationContext,"Sikeres regisztráció!",Toast.LENGTH_SHORT).show()
        }
    }
}