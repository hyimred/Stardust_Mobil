package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.ptrkcsak.stardust_mobil.Constans.EDITED_NOTE
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

class EditNoteActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        val note_text = findViewById<TextInputEditText>(R.id.note_text)
        val note_name = findViewById<TextInputEditText>(R.id.note_name)

        val submit = findViewById<Button>(R.id.submit)
        submit.setOnClickListener{
            val title = note_name.text.toString()
            val content = note_text.text.toString()
            editNote(EDITED_NOTE, title, content)
            startActivity(Intent(this@EditNoteActivity, MainActivity::class.java))
            EDITED_NOTE = ""
        }


        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener{
            startActivity(Intent(this@EditNoteActivity, MainActivity::class.java))
        }
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
                    Log.d("Pretty Printed JSON :", prettyJson)
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
}