package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.mikhaellopez.circularimageview.CircularImageView
import com.squareup.picasso.Picasso
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

class SettingsActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var navEmail: TextView
    private lateinit var EmailMain: TextView
    private lateinit var RegText: TextView
    private lateinit var NoteNum: TextView
    private lateinit var avatar: CircularImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var emailText: String = "Username"
    private var numText : Int = 0

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
        getLang()

        val changeEmail = findViewById<Button>(R.id.changeEmail)
        changeEmail.setOnClickListener{
            val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Change Email")
            val input = EditText(this)
            input.setHint("test@email.com")
            input.inputType = InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
            builder.setView(input)
            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                val newEmail = input.text.toString()
                MaterialAlertDialogBuilder(this)
                    .setTitle("Are you sure you want to change your email address?")
                    .setNegativeButton("No") { dialog, which ->
                    }
                    .setPositiveButton("Yes") { dialog, which ->
                        changeEmail(newEmail)
                        startActivity(Intent(this@SettingsActivity, LoadingActivity::class.java))
                        val sharedPreferences =
                            getSharedPreferences("Important", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("access_token", "")
                        editor.apply()
                    }
                    .show()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }

        val changePass = findViewById<Button>(R.id.changePass)
        changePass.setOnClickListener{
            val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Change Password")
            val input = EditText(this)
            input.setHint("Password")
            input.inputType = InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
            builder.setView(input)
            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                val newPass = input.text.toString()
                MaterialAlertDialogBuilder(this)
                    .setTitle("Are you sure you want to change your password?")
                    .setNegativeButton("No") { dialog, which ->
                    }
                    .setPositiveButton("Yes") { dialog, which ->
                        changePass(newPass)
                        startActivity(Intent(this@SettingsActivity, LoadingActivity::class.java))
                        val sharedPreferences =
                            getSharedPreferences("Important", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("access_token", "")
                        editor.apply()
                    }
                    .show()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()
        }

        val deleteAccount = findViewById<Button>(R.id.deleteAccount)
        deleteAccount.setOnClickListener{
            MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you want to delete your account?")
                .setNegativeButton("No") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Yes") { dialog, which ->
                    deleteProfile()
                    startActivity(Intent(this@SettingsActivity, LoadingActivity::class.java))
                    val sharedPreferences =
                        getSharedPreferences("Important", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("access_token", "")
                    editor.apply()
                }
                .show()
        }

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            navEmail = findViewById(R.id.email)
            navEmail.text = emailText
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
                        for (i in 0 until items.count()) {
                            if(items.isNotEmpty()){numText = items.count()}
                        }
                        NoteNum = findViewById(R.id.note_number)
                        NoteNum.text = numText.toString()
                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
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
                    if (user != null) {
                        emailText = user.email

                        EmailMain = findViewById(R.id.emailMain)
                        EmailMain.text = emailText
                        RegText = findViewById(R.id.registration_date)
                        val sm = SimpleDateFormat("yyyy. MM. dd.")
                        val strDate: String = sm.format(user.registartionDate)
                        RegText.text = strDate
                    }
                }
            }
        }
    }
    private fun deleteProfile() {
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
            val response = service.deleteProfile()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val user = response.body()
                }
                Log.d("DELETEPROFILE", response.code().toString())
            }
        }
    }
    private fun changeEmail(email: String) {
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
        jsonObject.put("email", email)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.updateProfile(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val user = response.body()
                }
                Log.d("CHANGEEMAIL", response.code().toString())
            }
        }
    }
    private fun changePass(password: String) {
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
        jsonObject.put("password", password)

        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.updateProfile(requestBody)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val user = response.body()
                }
                Log.d("CHANGEPASS", response.code().toString())
            }
        }
    }
}
