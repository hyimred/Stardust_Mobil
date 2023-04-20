package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.Locale

class NoteDataActivity : AppCompatActivity() {
    lateinit var noteId : String
    lateinit var title : String
    lateinit var content : String
    lateinit var previous : String

    lateinit var dateCreated : String
    lateinit var dateUpdated : String
    lateinit var dateArchived : String
    lateinit var dateDeleted : String

    private lateinit var dateCreatedTextView : TextView
    private lateinit var dateUpdatedTextView : TextView
    private lateinit var dateArchivedTextView : TextView
    private lateinit var dateDeletedTextView : TextView
    private lateinit var titleTextView: TextView
    private lateinit var contentTextView: TextView


    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_data)

        noteId = intent.getStringExtra("noteId").toString()
        title = intent.getStringExtra("title").toString()
        content = intent.getStringExtra("content").toString()
        previous = intent.getStringExtra("previous").toString()
        dateCreated = intent.getStringExtra("dateCreated").toString()
        dateArchived = intent.getStringExtra("dateArchived").toString()
        dateUpdated = intent.getStringExtra("dateUpdated").toString()
        dateDeleted = intent.getStringExtra("dateDeleted").toString()

        val noteIdText = findViewById<TextView>(R.id.noteId)
        noteIdText.text = noteId

        titleTextView = findViewById(R.id.title)
        val limitedTitle = if (title.length > 15) title.substring(0, 15) else title
        titleTextView.text = limitedTitle+"..."

        contentTextView = findViewById(R.id.desc)
        val limitedContent = if (content.length > 55) content.substring(0, 55) else content
        contentTextView.text = limitedContent+"..."

        dateCreatedTextView = findViewById(R.id.dateCreated)
        dateCreatedTextView.text = dateCreated

        dateUpdatedTextView = findViewById(R.id.dateUpdated)
        dateUpdatedTextView.text = dateUpdated

        dateArchivedTextView = findViewById(R.id.dateArchived)
        dateArchivedTextView.text = dateArchived

        dateDeletedTextView = findViewById(R.id.dateDeleted)
        dateDeletedTextView.text = dateDeleted

    }

}
