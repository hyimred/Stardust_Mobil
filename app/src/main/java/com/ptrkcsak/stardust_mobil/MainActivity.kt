package com.ptrkcsak.stardust_mobil

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.ListPopupWindow.MATCH_PARENT
import androidx.appcompat.widget.ListPopupWindow.WRAP_CONTENT
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.progressindicator.LinearProgressIndicator
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
class MainActivity : AppCompatActivity() {

    private val URLstring = "https://demonuts.com/Demonuts/JsonTest/Tennis/json_parsing.php"
    internal lateinit var dataModelArrayList: ArrayList<DataModel>
    private var rvAdapter: RvAdapter? = null
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout_root = findViewById<View>(R.id.layout_root_main) as View
        val animatedDrawable = layout_root.background as AnimationDrawable
        recyclerView = findViewById(R.id.recycler)
        fetchingJSON()

        animatedDrawable.setEnterFadeDuration(10)
        animatedDrawable.setExitFadeDuration(5000)
        animatedDrawable.start()

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)

        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    true
                }
                R.id.settings -> {
                    true
                }
                else -> false
            }
        }
    }
    private fun fetchingJSON() {

        showSimpleProgressDialog(this, "Loading...", "", false)

        val stringRequest = StringRequest(Request.Method.GET, URLstring,
            { response ->
                Log.d("strrrrr", ">>$response")

                try {

                    removeSimpleProgressDialog()

                    val obj = JSONObject(response)
                    if (obj.optString("status") == "true") {

                        dataModelArrayList = ArrayList()
                        val dataArray = obj.getJSONArray("data")

                        for (i in 0 until dataArray.length()) {

                            val playerModel = DataModel()
                            val dataobj = dataArray.getJSONObject(i)
                            playerModel.setNames(dataobj.getString("name"))
                            playerModel.setCountrys(dataobj.getString("country"))
                            playerModel.setCitys(dataobj.getString("city"))
                            dataModelArrayList.add(playerModel)
                        }
                        setupRecycler()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            })

        // request queue
        val requestQueue = Volley.newRequestQueue(this)

        requestQueue.add(stringRequest)
    }
    private fun setupRecycler() {

        rvAdapter = RvAdapter(this, dataModelArrayList)
        recyclerView!!.adapter = rvAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

    }
    companion object {
        private var mProgressDialog: ProgressDialog? = null

        fun removeSimpleProgressDialog() {
            try {
                if (mProgressDialog != null) {
                    if (mProgressDialog!!.isShowing) {
                        mProgressDialog!!.dismiss()
                        mProgressDialog = null
                    }
                }
            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()

            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        fun showSimpleProgressDialog(
            context: Context, title: String,
            msg: String, isCancelable: Boolean
        ) {
            try {
                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog.show(context, title, msg)
                    mProgressDialog!!.setCancelable(isCancelable)
                }

                if (!mProgressDialog!!.isShowing) {
                    mProgressDialog!!.show()
                }

            } catch (ie: IllegalArgumentException) {
                ie.printStackTrace()
            } catch (re: RuntimeException) {
                re.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}