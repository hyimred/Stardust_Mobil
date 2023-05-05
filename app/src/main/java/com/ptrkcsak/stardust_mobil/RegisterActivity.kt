package com.ptrkcsak.stardust_mobil

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import java.util.Locale

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

        getLang()

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
            var emailError = ""
            var passwordError = ""
            if (pwd == re_pwd){

                if (register_email.text.toString().isEmpty()) {
                    emailError += "The email mustn't be empty\n"
                }

                if (register_password.text.toString().isEmpty()) {
                    passwordError += "The password mustn't be empty\n"
                }

                val patternEmail = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
                if (!patternEmail.matches(email)) {
                    emailError += "Please enter a valid email\n"
                }

                val patternPassLen = Regex("^.{8,}\$")
                if (!patternPassLen.matches(pwd)) {
                    passwordError += "The password can't be shorter than 8 characters\n"
                }

                val patternPassWs = Regex("^(?!.\\s).\$")
                if (!patternPassWs.matches(pwd)) {
                    passwordError += "The password mustn't contain any whitespace characters\n"
                }

                val patternPassUc = Regex("^(?=.[A-Z]).\$")
                if (!patternPassUc.matches(pwd)) {
                    passwordError += "The password must contain at least 1 uppercase letter\n"
                }

                val patternPassLc = Regex("^(?=.[a-z]).\$")
                if (!patternPassLc.matches(pwd)) {
                    passwordError += "The password must contain at least 1 lowercase letter\n"
                }

                val patternPassNum = Regex("^(?=.\\d).\$")
                if (!patternPassNum.matches(pwd)) {
                    passwordError += "The password must contain at least 1 digit\n"
                }

                val patternPassSpec = Regex("^(?=.[!\"#\$%&'()+,-./:;<=>?@[\\\\]^`{|}~]).\\$")
                if (!patternPassSpec.matches(pwd)) {
                    passwordError += "The password must contain at least 1 special character (!\"#\$%&'()+,-./:;<=>?@[]^_`{|}~)\n"
                }

                val patternPassFull = Regex("(?<=^[A-Za-z0-9[!\\\"#\$%&'()*+,-./:;<=>?@\\\\\\\\[\\\\\\\\]^_`{|}~]]{0,7})[A-Za-z0-9[!\\\"#\$%&'()*+,-./:;<=>?@\\\\\\\\[\\\\\\\\]^_`{|}~]]{8,}(?<=[A-Za-z0-9[!\\\"#\$%&'()*+,-./:;<=>?@\\\\\\\\[\\\\\\\\]^_`{|}~]])")
                if (patternPassFull.matches(pwd) && patternEmail.matches(email)) {
                    Handler().postDelayed({
                        val intent =
                            Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                    }, SPLASH_TIME_OUT.toLong());
                    signup(email, pwd)
                }

                register_email.error = emailError
                register_password.error = passwordError
                register_re_password.error = passwordError

            } else {
                passwordError += "The passwords are different\n"
            }
        }
    }

    private fun getLang(){
        val newLanguage: String
        val prefs = getSharedPreferences("Important", Context.MODE_PRIVATE)
        newLanguage = prefs.getString("language", null).toString()
        val locale = Locale(newLanguage)
        Locale.setDefault(locale)
        val resources = resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
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