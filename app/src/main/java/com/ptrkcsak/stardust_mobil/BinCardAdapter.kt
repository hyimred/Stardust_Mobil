package com.ptrkcsak.stardust_mobil

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BinCardAdapter(private val mList: List<ItemsViewModel>, private val context: Context) : RecyclerView.Adapter<BinCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bin_note, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        val activity = BinActivity()
        holder.title.text = ItemsViewModel.title
        holder.desc.text = ItemsViewModel.desc

        holder.unbinNote.setOnClickListener{
            activity.unbinNote(ItemsViewModel.noteId)
            val intent = Intent(context, BinActivity::class.java)
            context.startActivity(intent)
        }

        holder.archiveNote.setOnClickListener{
            activity.archiveNote(ItemsViewModel.noteId)
            val intent = Intent(context, BinActivity::class.java)
            context.startActivity(intent)
        }

        holder.deleteNote.setOnClickListener{
            MaterialAlertDialogBuilder(context)
                .setTitle("Are you sure you want to delete this note?")
                .setNegativeButton("No") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Yes") { dialog, which ->
                    activity.deleteNote(ItemsViewModel.noteId)
                    val intent = Intent(context, BinActivity::class.java)
                    context.startActivity(intent)
                }
                .show()
        }

    }
    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val desc: TextView = itemView.findViewById(R.id.desc)
        val deleteNote: Button = itemView.findViewById(R.id.deleteNote)
        val archiveNote: Button = itemView.findViewById(R.id.archiveNote)
        val unbinNote: Button = itemView.findViewById(R.id.unbinNote)
    }

}