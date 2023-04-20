package com.ptrkcsak.stardust_mobil

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ArchiveCardAdapter(private val mList: List<ItemsViewModel>, private val context: Context) : RecyclerView.Adapter<ArchiveCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.archive_note, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]
        val activity = ArchiveActivity()
        holder.title.text = ItemsViewModel.title
        holder.desc.text = ItemsViewModel.desc

        holder.unarchiveNote.setOnClickListener{
            activity.unarchiveNote(ItemsViewModel.noteId)
            val intent = Intent(context, ArchiveActivity::class.java)
            context.startActivity(intent)
        }

        holder.deleteNote.setOnClickListener{
            activity.binNote(ItemsViewModel.noteId)
            val intent = Intent(context, ArchiveActivity::class.java)
            context.startActivity(intent)
        }

        holder.card.setOnClickListener {
            val intent = Intent(context, NoteDataActivity::class.java)
            intent.putExtra("previous", activity.javaClass.simpleName)
            intent.putExtra("noteId", ItemsViewModel.noteId)
            intent.putExtra("title", ItemsViewModel.title)
            intent.putExtra("content", ItemsViewModel.desc)
            intent.putExtra("dateCreated", ItemsViewModel.dateCreated)
            intent.putExtra("dateArchived", ItemsViewModel.dateArchived)
            intent.putExtra("dateUpdated", ItemsViewModel.dateUpdated)
            intent.putExtra("dateDeleted", ItemsViewModel.dateDeleted)
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
        val unarchiveNote: Button = itemView.findViewById(R.id.unarchiveNote)
        val deleteNote: Button = itemView.findViewById(R.id.deleteNote)
    }

}