package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class SettingsActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var navEmail: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var noteNumber: TextView
    private lateinit var registerDate: TextView
    lateinit var emailText: String
    lateinit var regText: String
    var numText: Int = 0

    private val SPLASH_TIME_OUT = 1000;
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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

        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        navView = findViewById(R.id.navView)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    startActivity(Intent(this@SettingsActivity, MainActivity::class.java))
                    true
                }

                R.id.archive -> {
                    startActivity(Intent(this@SettingsActivity, ArchiveActivity::class.java))
                    true
                }

                R.id.bin -> {
                    startActivity(Intent(this@SettingsActivity, BinActivity::class.java))
                    true
                }

                R.id.devlog -> {
                    true
                }

                R.id.settings -> {
                    startActivity(Intent(this@SettingsActivity, SettingsActivity::class.java))
                    true
                }

                R.id.logout -> {
                    startActivity(Intent(this@SettingsActivity, LoadingActivity::class.java))
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

        val language = findViewById<TextInputLayout>(R.id.language)
        val listLang = listOf("English", "Deusch", "JP", "Magyar")
        val adapterLang = ArrayAdapter(this, R.layout.list_item, listLang)
        (language.editText as? AutoCompleteTextView)?.setAdapter(adapterLang)

        val theme = findViewById<TextInputLayout>(R.id.theme)
        val listTheme = listOf("Blue", "Red", "Green", "Purple", "Yellow", "Gray", "Pink")
        val adapterTheme = ArrayAdapter(this, R.layout.list_item, listTheme)
        (theme.editText as? AutoCompleteTextView)?.setAdapter(adapterTheme)

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
                        }
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
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
