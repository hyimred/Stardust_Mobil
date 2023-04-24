package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.ptrkcsak.stardust_mobil.*
import com.ptrkcsak.stardust_mobil.Constans.BASE_URL
import com.ptrkcsak.stardust_mobil.Constans.USER_TOKEN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var navEmail: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var noteNumber: TextView
    private lateinit var registerDate: TextView
    lateinit var emailText: String
    lateinit var regText: String
    var numText : Int = 0

    val SPLASH_TIME_OUT = 1000;
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("Important", Context.MODE_PRIVATE)
        val token = prefs.getString("access_token", null)
        USER_TOKEN = token.toString()

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

            drawerLayout.openDrawer(navView)

            if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.drawerLayout.closeDrawer(GravityCompat.START)
                super.onBackPressed()
            }
        }

        val btn_new = findViewById<FloatingActionButton>(R.id.btn_new)
        val popup = PopupMenu(this, btn_new)
        popup.menuInflater.inflate(R.menu.menu_new, popup.menu)

        btn_new.setOnClickListener{
            popup.show()
        }

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.new_note -> {
                    startActivity(Intent(this@MainActivity, NewActivity::class.java))
                    true
                }
                R.id.new_god -> {
                    lifecycleScope.launch {
                        whisperGod()
                    }
                    true
                }
                R.id.new_qr -> {
                    startActivity(Intent(this@MainActivity, NewByQrActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    true
                }
                else -> false
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
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    true
                }
                R.id.archive -> {
                    startActivity(Intent(this@MainActivity, ArchiveActivity::class.java))
                    true
                }
                R.id.bin -> {
                    startActivity(Intent(this@MainActivity, BinActivity::class.java))
                    true
                }
                R.id.devlog -> {
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    true
                }
                R.id.logout -> {
                    startActivity(Intent(this@MainActivity, LoadingActivity::class.java))
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
    private suspend fun whisperGod() {
        val interceptor = TokenInterceptor()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiInterface::class.java)
        val response = service.postGod()
        if (response.isSuccessful) {
            recreate()
            Toast.makeText(this@MainActivity, "The words of God have reached you successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "The words of God can't reach you! You dirty disbeliever!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getNotes() {
        val interceptor = TokenInterceptor()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
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
                            Log.d("BINNED", response.body().toString())
                            val noteId = items[i].noteId
                            val title = items[i].title
                            val content = items[i].content
                            val isArchived = items[i].isArchived
                            val dateArchived = items[i].dateArchived
                            val isBinned = items[i].isBinned
                            val dateBinned = items[i].dateBinned
                            val dateCreated = items[i].dateCreated
                            val dateUpdated = items[i].dateUpdated
                            if(!items[i].isArchived && !items[i].isBinned){
                                data.add(ItemsViewModel(title, content, noteId, dateCreated, dateUpdated, dateArchived, dateBinned, isArchived, isBinned))
                                Log.e("DATA ADD", data.toString())
                            }
                        }
                        val adapter = MainCardAdapter(data, this@MainActivity)
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }
    fun binNote(noteId: String) {
        val interceptor = TokenInterceptor()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiInterface::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.binNote(noteId)
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
    fun archiveNote(noteId: String) {
        val interceptor = TokenInterceptor()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
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
    @SuppressLint("SimpleDateFormat")
    private fun getProfile() {
        val interceptor = TokenInterceptor()
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
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