package com.example.quoteapp.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteapp.ModelClass.ModelClassQuote
import com.example.quoteapp.ModelClass.ModelClassQuotePayload
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Utililty

class AdapterRvQuote(val list: ArrayList<Pair<String,String>>):RecyclerView.Adapter<AdapterRvQuote.ViewHolder>() {

    class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val quote = itemview.findViewById<TextView>(R.id.quote)
        val icon = itemview.findViewById<ImageView>(R.id.icon)
        val title = itemview.findViewById<TextView>(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.layout_quote_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Utililty().updateTextSizeRecursive(holder.itemView)
        when(list[position].first){
            "personal_perspective"->{
                holder.icon.setImageResource(R.drawable.ic_personal_pp)
                holder.quote.text=list[position].second
                holder.title.text="Personal Perspetive"

            }
            "bio"->{
                holder.icon.setImageResource(R.drawable.ic_edit)
                holder.quote.text=list[position].second
                holder.title.text="Brief Biography"

            }
            "Thoughts To Start The Day"->{
                holder.icon.setImageResource(R.drawable.ic_morning)
                holder.quote.text=list[position].second
                holder.title.text=list[position].first

            }
            "Thoughts To End The Day"->{
                holder.icon.setImageResource(R.drawable.ic_evening)
                holder.quote.text=list[position].second
                holder.title.text=list[position].first

            }

        }
    }
}