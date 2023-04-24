package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
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
import java.text.SimpleDateFormat
import java.util.Locale

class EditNoteActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        val dateCreated = findViewById<TextView>(R.id.dateCreated)
        val dateUpdated = findViewById<TextView>(R.id.dateUpdated)
        val note_ID = findViewById<TextView>(R.id.noteId)
        val note_name = findViewById<TextInputEditText>(R.id.note_name)
        val note_text = findViewById<TextInputEditText>(R.id.note_text)
        val noteId = intent.getStringExtra("noteId").toString()

        getLang()

        Log.d("retrofit ID", noteId)

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

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getNote(noteId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item != null) {
                        Log.d("ITEM EDITNOTE", response.body().toString())
                        note_ID.text = item.noteId
                        note_text.setText(item.content)
                        note_name.setText(item.title)
                        val formatter = SimpleDateFormat("yyyy. MMMM dd.\n HH:mm:ss", Locale.getDefault())
                        val dateCreatedString = item.dateCreated.let { formatter.format(it) }
                        val dateUpdatedString = item.dateCreated.let { formatter.format(it) }
                        dateCreated.text = dateCreatedString.toString()
                        dateUpdated.text = dateUpdatedString.toString()
                    }
                } else {
                    Log.e("ITEM EDITNOTE", response.code().toString())
                }
            }
        }

        val submit = findViewById<Button>(R.id.submit)
        submit.setOnClickListener{
            val title = note_name.text.toString()
            val content = note_text.text.toString()
            editNote(noteId, title, content)
            startActivity(Intent(this@EditNoteActivity, MainActivity::class.java))
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener{
            startActivity(Intent(this@EditNoteActivity, MainActivity::class.java))
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
    fun editNote(noteId: String, title: String, content: String) {
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
        jsonObject.put("noteId", noteId)
        jsonObject.put("title", title)
        jsonObject.put("content", content)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.editNote(noteId, requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )
                    Toast.makeText(this@EditNoteActivity, "Update Successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
}