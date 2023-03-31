package com.ptrkcsak.stardust_mobil

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class NewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        val eventDate = findViewById<Button>(R.id.date_picker)
        val eventDateText = findViewById<TextInputEditText>(R.id.event_date_pick)

        eventDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    eventDateText.setText(dat)
                }, year, month, day)
            datePickerDialog.show()
        }

        val submit = findViewById<Button>(R.id.submit)
        submit.setOnClickListener{
            startActivity(Intent(this@NewActivity, MainActivity::class.java))
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener{
            startActivity(Intent(this@NewActivity, MainActivity::class.java))
        }
    }
}