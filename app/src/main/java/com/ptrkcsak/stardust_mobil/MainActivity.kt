package com.ptrkcsak.stardust_mobil

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
class MainActivity : AppCompatActivity() {
    val SPLASH_TIME_OUT = 1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_menu = findViewById<Button>(R.id.btn_menu)
        val layout_root = findViewById<View>(R.id.layout_root_main) as View
        val animatedDrawable = layout_root.background as AnimationDrawable

        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(5000)
        animatedDrawable.start()

        btn_menu.setOnClickListener {
            showPopup(btn_menu)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.categories -> {

                }
                R.id.profile -> {
                    Handler().postDelayed({
                        val intent =
                            Intent(this@MainActivity, ProfileActivity::class.java)
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }, SPLASH_TIME_OUT.toLong())
                }
                R.id.settings -> {
                    Handler().postDelayed({
                        val intent =
                            Intent(this@MainActivity, SettingsActivity::class.java)
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }, SPLASH_TIME_OUT.toLong())
                }
            }
            true
        })
        popup.show()
    }


}