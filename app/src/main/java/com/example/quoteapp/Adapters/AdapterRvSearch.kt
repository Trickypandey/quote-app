package com.example.quoteapp.Adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.quoteapp.Interface.RvClickInterface
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Utililty

class AdapterRvSearch(val list: ArrayList<Triple<String,String, String>>, val click:RvClickInterface): RecyclerView.Adapter<AdapterRvSearch.ViewHolder>() {
    private var isLoading: Boolean = false
    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val quote = itemview.findViewById<TextView>(R.id.quote)
        val time = itemview.findViewById<TextView>(R.id.datetimequote)
        val icon = itemview.findViewById<ImageView>(R.id.icon_search)
        val title = itemview.findViewById<TextView>(R.id.title)
        val progressDai = itemView.findViewById<ProgressBar>(R.id.progress_dai)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        Log.e("all list ", list.toString())
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_quote_search, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        Log.e("date", list.toString())
        return list.size
    }


    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }
    fun addData(newData: ArrayList<Triple<String, String, String>>) {
        list.addAll(newData)
        notifyDataSetChanged()
    }

    fun setLoading(loading: Boolean) {
        isLoading = loading
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Utililty().updateTextSizeRecursive(holder.itemView)

        // Check if it's the last item and show/hide the ProgressBar accordingly
        if (position == list.size - 1 && isLoading) {
            holder.progressDai.visibility = View.VISIBLE
        } else {
            holder.progressDai.visibility = View.GONE
        }
        when(list[position].first){
            "quote"->{
                holder.icon.setImageResource(R.drawable.ic_double_quote)
                holder.quote.text=list[position].second
                holder.title.text="Quote"
                holder.time.text=Utililty().formatDate(list[position].third)
            }
            "personal_perspective"->{
                holder.icon.setImageResource(R.drawable.ic_personal_pp)
                holder.quote.text=list[position].second
                holder.title.text="Personal Perspetive"
                holder.time.text=Utililty().formatDate(list[position].third)
            }
            "bio"->{
                holder.icon.setImageResource(R.drawable.ic_edit)
                holder.quote.text=list[position].second
                holder.title.text="Brief Biography"
                holder.time.text=Utililty().formatDate(list[position].third)
            }
            "fact"->{
                holder.icon.setImageResource(R.drawable.ic_fact)
                holder.quote.text=list[position].second
                holder.title.text="Fact"
                holder.time.text=list[position].third
            }
            "Thoughts To Start The Day"->{
                holder.icon.setImageResource(R.drawable.ic_morning)
                holder.quote.text=list[position].second
                holder.title.text=list[position].first
                holder.time.text=Utililty().formatDate(list[position].third)

            }
            "Thoughts To End The Day"->{
                holder.icon.setImageResource(R.drawable.ic_evening)
                holder.quote.text=list[position].second
                holder.title.text=list[position].first
                holder.time.text=Utililty().formatDate(list[position].third)


            }
        }
        holder.itemView.setOnClickListener {
            click.onItemClick(list[position].third,holder.title.text.toString(),list[position].second)
        }

    }

}