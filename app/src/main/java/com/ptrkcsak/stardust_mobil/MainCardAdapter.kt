package com.ptrkcsak.stardust_mobil

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.util.Locale

class MainCardAdapter(private val mList: List<ItemsViewModel>, private val context: Context) : RecyclerView.Adapter<MainCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_note, parent, false)
        return ViewHolder(view)
    }
    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        val activity = MainActivity()
        holder.title.text = ItemsViewModel.title
        holder.desc.text = ItemsViewModel.desc

        holder.editNote.setOnClickListener{
            val intent = Intent(context, EditNoteActivity::class.java)
            intent.putExtra("noteId", holder.editNote.tag as String)
            context.startActivity(intent)
        }
        holder.exportNote.setOnClickListener{
            val intent = Intent(context, ExportNoteActivity::class.java)
            intent.putExtra("noteId", holder.exportNote.tag as String)
            context.startActivity(intent)
        }
        holder.archiveNote.setOnClickListener{
            activity.archiveNote(ItemsViewModel.noteId)
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

        holder.deleteNote.setOnClickListener{
            activity.binNote(ItemsViewModel.noteId)
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }



        holder.card.setOnClickListener {
            val intent = Intent(context, NoteDataActivity::class.java)
            intent.putExtra("previous", activity.javaClass.simpleName)
            intent.putExtra("noteId", ItemsViewModel.noteId)
            intent.putExtra("title", ItemsViewModel.title)
            intent.putExtra("content", ItemsViewModel.desc)

            /*
            val formatter = SimpleDateFormat("yyyy.\nMMMM dd.")
            val formattedDateCreated = ItemsViewModel.dateCreated.let { formatter.format(it) }
            val formattedDateArchived = ItemsViewModel.dateArchived.let { formatter.format(it) }
            val formattedDateUpdated = ItemsViewModel.dateUpdated.let { formatter.format(it) }
            val formattedDateDeleted = ItemsViewModel.dateDeleted.let { formatter.format(it) }
            intent.putExtra("dateCreated", formattedDateCreated)
            intent.putExtra("dateArchived", formattedDateArchived)
            intent.putExtra("dateUpdated", formattedDateUpdated)
            intent.putExtra("dateDeleted", formattedDateDeleted)
             */

            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val card: MaterialCardView = itemView.findViewById(R.id.card_view)
        val title: TextView = itemView.findViewById(R.id.title)
        val desc: TextView = itemView.findViewById(R.id.desc)
        val editNote: Button = itemView.findViewById(R.id.editNote)
        val archiveNote: Button = itemView.findViewById(R.id.archiveNote)
        val exportNote: Button = itemView.findViewById(R.id.exportNote)
        val deleteNote: Button = itemView.findViewById(R.id.deleteNote)
    }

}
