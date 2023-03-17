package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var navHeader: NavigationView
    private lateinit var navEmail: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

        bottomAppBar.setNavigationOnClickListener {
            navEmail = findViewById(R.id.email)
            navEmail.setText(getIntent().getExtras()?.getString("login_email"))
            drawerLayout.openDrawer(navView)
            if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.drawerLayout.closeDrawer(GravityCompat.START)
                super.onBackPressed()
            }
        }

        val btn_new = findViewById<Button>(R.id.btn_new)

        btn_new.setOnClickListener{
            val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Új esemény")

            val event_title = EditText(this)
            event_title.setHint("Esemény neve")
            event_title.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(event_title)

            val event_content = EditText(this)
            event_content.setHint("Esemény leírása")
            event_content.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(event_content)

            val event_date = EditText(this)
            event_date.setHint("Esemény ideje")
            event_date.inputType = InputType.TYPE_CLASS_DATETIME
            builder.setView(event_date)

            val event_type = EditText(this)
            event_type.setHint("Esemény típusa")
            event_type.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(event_type)

            val event_categories = EditText(this)
            event_categories.setHint("Esemény típusa")
            event_categories.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(event_categories)

            builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                var event_name_text = event_title.text.toString()
                var event_content_text = event_content.text.toString()
                var event_date_text = event_date.text.toString()
                var event_type_text = event_type.text.toString()
                var event_categories_text = event_categories.text.toString()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

            builder.show()
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarToggle.syncState()
        navView = findViewById(R.id.navView)
        navHeader = findViewById(R.id.navConstraint)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this@MainActivity,ProfileActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this@MainActivity,SettingsActivity::class.java))
                    true
                }
                R.id.logout -> {
                    startActivity(Intent(this@MainActivity,LoadingActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

}