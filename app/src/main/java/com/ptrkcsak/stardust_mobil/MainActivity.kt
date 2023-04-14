package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
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

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var navEmail: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var emailText: String

    val SPLASH_TIME_OUT = 1000;
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

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

        bottomAppBar.setNavigationOnClickListener {
            navEmail = findViewById(R.id.email)
            navEmail.setText(emailText)
            drawerLayout.openDrawer(navView)
            if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.drawerLayout.closeDrawer(GravityCompat.START)
                super.onBackPressed()
            }
        }

        val btn_new = findViewById<FloatingActionButton>(R.id.btn_new)
        val popupMenu = PopupMenu(this, btn_new)
        popupMenu.inflate(R.menu.menu_new)

        btn_new.setOnClickListener{
            popupMenu.show()
        }

        popupMenu.setOnMenuItemClickListener { menuItem ->
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
                R.id.settings -> {
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                    true
                }
                R.id.logout -> {
                    startActivity(Intent(this@MainActivity, LoadingActivity::class.java))
                    val sharedPreferences =
                        getSharedPreferences("Important", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("access_token", "")
                    editor.commit()
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
    suspend fun whisperGod() {
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
            Toast.makeText(this@MainActivity, "Sikeres Generálás!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "Sikertelen Generálás!", Toast.LENGTH_SHORT).show()
        }
    }
    fun getNotes() {
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
                            val noteId = items[i].noteId
                            val title = items[i].title
                            val content = items[i].content

                            data.add(ItemsViewModel(title, content, noteId))
                            Log.d("noteID 1", noteId)
                        }
                        val adapter = CardAdapter(data, this@MainActivity)
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
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
            .baseUrl(BASE_URL)
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
                    Log.d("Pretty Printed JSON :", prettyJson)
                } else {
                }
            }
        }
    }
    fun getProfile() {
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
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }

    }
}
