package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class ProfileActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var navEmail: TextView
    private lateinit var navEmailMain: TextView
    private lateinit var noteNumber: TextView
    private lateinit var registerDate: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var emailText: String
    val SPLASH_TIME_OUT = 1000;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        noteNumber = findViewById(R.id.note_number)
        registerDate = findViewById(R.id.registration_date)
        navEmailMain = findViewById(R.id.email_main)

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

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

        bottomAppBar.setNavigationOnClickListener {
            navEmail = findViewById(R.id.email)
            navEmail.text = emailText
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
                    startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java))
                    true
                }
                R.id.logout -> {
                    startActivity(Intent(this@ProfileActivity, LoadingActivity::class.java))
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

    }
    fun getNotes() {
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
                        noteNumber.text = items.count().toString()
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
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
                    navEmailMain.text = emailText

                    val formatter = SimpleDateFormat("yyyy.\nMMMM dd.")
                    val formattedDate = formatter.format(user?.registartionDate)

                    registerDate.text = formattedDate
                    Log.d("REGISTER DATE", user.toString())
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }

    }
}

