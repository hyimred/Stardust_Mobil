package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.ptrkcsak.stardust_mobil.databinding.ActivityExportNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream


class ExportNoteActivity : AppCompatActivity() {
    lateinit var bitmap: Bitmap
    private var binding: ActivityExportNoteBinding? = null

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val noteId = intent.getStringExtra("noteId").toString()
        var qrText = ""
        val qr: ImageView = findViewById(R.id.qr)

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
                        bitmap = encoder.encodeBitmap(qrText, BarcodeFormat.QR_CODE, 400, 400)
                        qr.setImageBitmap(bitmap)
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }

        val share = findViewById<Button>(R.id.share)
        share.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"

            val uri = getImageUri(bitmap)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.putExtra(Intent.EXTRA_TEXT, "Stardust Notepad - Exported Note")
            startActivity(Intent.createChooser(intent, "Share Image"))
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener{
            startActivity(Intent(this@ExportNoteActivity, MainActivity::class.java))
        }
    }
    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "stardust_exported_note", null)
        return Uri.parse(path)
    }

}