package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.gson.Gson
import com.ptrkcsak.stardust_mobil.Constans.BASE_URL
import com.ptrkcsak.stardust_mobil.Constans.USER_TOKEN
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    val SPLASH_TIME_OUT = 1;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

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
        val login_email = findViewById<View>(R.id.login_email) as EditText
        val login_password = findViewById<View>(R.id.login_password) as EditText

        btn_submit.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_password.text.toString()
            signin(email, password)
        }

        val setIP = findViewById<Button>(R.id.setIP)
        setIP.setOnClickListener{
            val dialogView = LayoutInflater.from(this).inflate(R.layout.set_ip, null)
            val editText = dialogView.findViewById<EditText>(R.id.edit_text)
            val okButton = dialogView.findViewById<Button>(R.id.ok_button)

            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            editText.setText(BASE_URL)

            okButton.setOnClickListener {
                BASE_URL = editText.text.toString()
                dialog.dismiss()
            }
            dialog.show()
        }
    }
    private fun signin(email: String, password: String){
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
        val signInInfo = SignInBody(email, password)
        retIn.signin(signInInfo).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200 || response.code() == 201) {
                    Toast.makeText(this@LoginActivity, "Login success!", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed({
                        val intent =
                            Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        val converter = Gson()

                        val token: TokenHelper = converter.fromJson(
                            response.body()?.string(),
                            TokenHelper::class.java
                        )
                        val sharedPreferences =
                            getSharedPreferences("Important", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("access_token", token.getToken())
                        editor.commit()

                        USER_TOKEN = token.access_token
                        Log.d("USER_TOKEN", "onResponse: $USER_TOKEN")

                    }, SPLASH_TIME_OUT.toLong());
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
