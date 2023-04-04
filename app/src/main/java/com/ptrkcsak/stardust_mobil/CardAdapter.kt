package com.ptrkcsak.stardust_mobil

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_one, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ItemsViewModel = mList[position]

        holder.title.text = ItemsViewModel.title
        holder.desc.text = ItemsViewModel.desc
        holder.editNote.tag = ItemsViewModel.noteId
        holder.exportNote.tag = ItemsViewModel.noteId
        holder.deleteNote.tag = ItemsViewModel.noteId
        Log.d("noteID CardAdapter", holder.deleteNote.tag.toString())
        holder.editNote.setOnClickListener{
        }
        holder.exportNote.setOnClickListener{
        }
        holder.deleteNote.setOnClickListener{
            val activity = MainActivity()
            val noteId = holder.deleteNote.getTag().toString()
            activity.deleteNote(noteId)
        }
    }
    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val desc: TextView = itemView.findViewById(R.id.desc)
        val editNote: Button = itemView.findViewById(R.id.editNote)
        val exportNote: Button = itemView.findViewById(R.id.exportNote)
        val deleteNote: Button = itemView.findViewById(R.id.deleteNote)
    }

}
