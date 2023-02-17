package com.ptrkcsak.stardust_mobil

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class RvAdapter(ctx: Context, private val dataModelArrayList: ArrayList<DataModel>) :
    RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.MyViewHolder {
        val view = inflater.inflate(R.layout.rv_one, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvAdapter.MyViewHolder, position: Int) {
        holder.name.setText(dataModelArrayList[position].getNames())
        holder.country.setText(dataModelArrayList[position].getCountrys())
        holder.city.setText(dataModelArrayList[position].getCitys())
    }

    override fun getItemCount(): Int {
        return dataModelArrayList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var country: TextView
        var name: TextView
        var city: TextView
        init {
            country = itemView.findViewById<View>(R.id.country) as TextView
            name = itemView.findViewById<View>(R.id.name) as TextView
            city = itemView.findViewById<View>(R.id.city) as TextView
        }
    }
}