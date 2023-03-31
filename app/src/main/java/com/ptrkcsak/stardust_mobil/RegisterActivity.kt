package com.ptrkcsak.stardust_mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class RegisterActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

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
        val register_email = findViewById<View>(R.id.register_email) as EditText
        val register_password = findViewById<View>(R.id.register_password) as EditText
        val register_re_password = findViewById<View>(R.id.register_re_password) as EditText

        btn_submit.setOnClickListener {
            val email = register_email.text.toString()
            val pwd = register_password.text.toString()
            val re_pwd = register_re_password.text.toString()
            if (pwd == re_pwd){
                Handler().postDelayed({
                    val intent =
                        Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                }, SPLASH_TIME_OUT.toLong());
                signup(email, pwd)
            } else {Toast.makeText(this@RegisterActivity, "A két jelszó különbözik!", Toast.LENGTH_SHORT)
                .show()
            }
        }
    }

    private fun signup(email: String, password: String){
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val registerInfo = UserBody(email,password)

        retIn.registerUser(registerInfo).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@RegisterActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()

                println(t.message)
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    Toast.makeText(this@RegisterActivity, "Registration success!", Toast.LENGTH_SHORT)
                        .show()

                }
                else{
                    Toast.makeText(this@RegisterActivity, "Registration failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }
}