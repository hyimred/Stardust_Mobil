package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class BinActivity : AppCompatActivity() {
        private lateinit var recyclerView: RecyclerView
        private lateinit var drawerLayout: DrawerLayout
        private lateinit var actionBarToggle: ActionBarDrawerToggle
        private lateinit var navView: NavigationView
        private lateinit var navEmail: TextView
        private lateinit var avatar: CircularImageView
        private lateinit var noteNumber: TextView
        private lateinit var registerDate: TextView
        private lateinit var swipeRefreshLayout: SwipeRefreshLayout
        lateinit var emailText: String
        lateinit var regText: String
        var numText : Int = 0

        val SPLASH_TIME_OUT = 1000;
        @RequiresApi(Build.VERSION_CODES.Q)
        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_bin)

            val prefs = getSharedPreferences("Important", Context.MODE_PRIVATE)
            val token = prefs.getString("access_token", null)
            Constans.USER_TOKEN = token.toString()

            val windowInsetsController =
                WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

            getNotes()
            getProfile()
            getLang()

            val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

            bottomAppBar.setNavigationOnClickListener {
                navEmail = findViewById(R.id.email)
                navEmail.text = emailText
                noteNumber = findViewById(R.id.note_number)
                noteNumber.text = numText.toString()
                registerDate = findViewById(R.id.registration_date)
                registerDate.text = regText
                avatar = findViewById(R.id.avatar)
                Picasso.get().load("https://robohash.org/$emailText").into(avatar)
                drawerLayout.openDrawer(navView)
                if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    this.drawerLayout.closeDrawer(GravityCompat.START)
                    super.onBackPressed()
                }
            }

            drawerLayout = findViewById(R.id.drawerLayout)
            actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
            drawerLayout.addDrawerListener(actionBarToggle)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            actionBarToggle.syncState()
            navView = findViewById(R.id.navView)

            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.home -> {
                        startActivity(Intent(this@BinActivity, MainActivity::class.java))
                        true
                    }
                    R.id.archive -> {
                        startActivity(Intent(this@BinActivity, ArchiveActivity::class.java))
                        true
                    }
                    R.id.bin -> {
                        startActivity(Intent(this@BinActivity, BinActivity::class.java))
                        true
                    }
                    R.id.devlog -> {
                        true
                    }
                    R.id.settings -> {
                        true
                    }
                    R.id.logout -> {
                        startActivity(Intent(this@BinActivity, LoadingActivity::class.java))
                        val sharedPreferences =
                            getSharedPreferences("Important", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("access_token", "")
                        editor.apply()
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            swipeRefreshLayout = findViewById(R.id.swiperefresh)
            swipeRefreshLayout.setOnRefreshListener {
                Handler().postDelayed({
                    recreate()
                    swipeRefreshLayout.isRefreshing = false
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }, SPLASH_TIME_OUT.toLong())
            }

            recyclerView = findViewById(R.id.recycler)
            recyclerView.layoutManager = LinearLayoutManager(this)
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
        private fun getNotes() {
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
                val response = service.getAllNotes()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val items = response.body()
                        if (items != null) {
                            val data = ArrayList<ItemsViewModel>()
                            for (i in 0 until items.count()) {
                                if(items.isNotEmpty()){numText = items.count()}
                                val noteId = items[i].noteId
                                val title = items[i].title
                                val content = items[i].content
                                val isArchived = items[i].isArchived
                                val dateArchived = items[i].dateArchived
                                val isBinned = items[i].isBinned
                                val dateBinned = items[i].dateBinned
                                val dateCreated = items[i].dateCreated
                                val dateUpdated = items[i].dateUpdated
                                if(items[i].isBinned){
                                    data.add(ItemsViewModel(title, content, noteId, dateCreated, dateUpdated, dateArchived, dateBinned, isArchived, isBinned))
                                }
                            }
                            val adapter = BinCardAdapter(data, this@BinActivity)
                            recyclerView.adapter = adapter
                        }
                    }
                }
            }
        }
        fun deleteNote(noteId: String) {
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
                val response = service.deleteNote(noteId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(
                            JsonParser.parseString(
                                response.body()
                                    ?.string()
                            )
                        )
                    }
                }
            }
        }
        fun archiveNote(noteId: String) {
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
                val response = service.archiveNote(noteId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(
                            JsonParser.parseString(
                                response.body()
                                    ?.string()
                            )
                        )
                    }
                }
            }
        }

        fun unbinNote(noteId: String) {
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
                val response = service.unbinNote(noteId)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson = gson.toJson(
                            JsonParser.parseString(
                                response.body()
                                    ?.string()
                            )
                        )
                    }
                }
            }
        }
        @SuppressLint("SimpleDateFormat")
        private fun getProfile() {
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
                val response = service.getProfile()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        emailText = user?.email.toString()
                        val formatter = SimpleDateFormat("yyyy.\nMMMM dd.")
                        val formattedDate = user?.registartionDate?.let { formatter.format(it) }
                        regText = formattedDate.toString()
                    }
                }
            }
        }
    }