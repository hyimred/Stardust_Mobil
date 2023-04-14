package com.ptrkcsak.stardust_mobil

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExportNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_note)

        val noteId = intent.getStringExtra("noteId").toString()
        var qrText = ""
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
                        val jsonObject = JSONObject()
                        jsonObject.put("title", item.title)
                        jsonObject.put("content", item.content)
                        qrText = jsonObject.toString()
                        Log.d("QRTEXT", qrText)
                        val encoder = BarcodeEncoder()
                        val bitmap = encoder.encodeBitmap(qrText, BarcodeFormat.QR_CODE, 400, 400)
                        val qr: ImageView = findViewById(R.id.qr)
                        qr.setImageBitmap(bitmap)
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener{
            startActivity(Intent(this@ExportNoteActivity, MainActivity::class.java))
        }
    }
}