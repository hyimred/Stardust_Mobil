package com.ptrkcsak.stardust_mobil

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class NewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        val note_text = findViewById<TextInputEditText>(R.id.note_text)
        val note_name = findViewById<TextInputEditText>(R.id.note_name)
        getLang()

        val submit = findViewById<Button>(R.id.submit)
        submit.setOnClickListener{
            val title = note_name.text.toString()
            val content = note_text.text.toString()
            if (title == "" || content == "") {
                Toast.makeText(this@NewActivity, "The title and the content musn't be empty!", Toast.LENGTH_SHORT).show()
            } else {
                newNote(title, content)
                startActivity(Intent(this@NewActivity, MainActivity::class.java))
            }
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener{
            startActivity(Intent(this@NewActivity, MainActivity::class.java))
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
    fun newNote(title: String, content: String) {
        val interceptor = TokenInterceptor()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constans.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiInterface::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("content", content)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.postNote(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )
                    Toast.makeText(this@NewActivity, "Create Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@NewActivity, "Create Error! ERROR: "+response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}