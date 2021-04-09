package com.example.movieapp.extension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie

class GenreAdapter(private val genreList: List<Genre>,
                   private val cellClickListener: GenreAdapter.CellClickListener):
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    interface CellClickListener {
        fun onCellClickListener(data: Genre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val rowNotification = inflater.inflate(R.layout.row_genre, parent, false)

        return ViewHolder(rowNotification)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = this.genreList.get(position)

        holder.bind(genre)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(genre)
        }
    }

    override fun getItemCount(): Int {
        return this.genreList.size
    }

    // view-holder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var title = view.findViewById<TextView>(R.id.tv_genre)

        fun bind(item: Genre) {
            title.text = item.name
        }
    }
}