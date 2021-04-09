package com.example.movieapp.extension.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.core.constant.API
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class MovieAdapter(private val movieList: List<Movie>,
                   private val cellClickListener: MovieAdapter.CellClickListener):
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    interface CellClickListener {
        fun onCellClickListener(data: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(R.layout.row_movie, parent, false)

        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = this.movieList.get(position)

        holder.bind(movie)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(movie)
        }
    }

    override fun getItemCount(): Int {
        return this.movieList.size
    }

    // view-holder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.tv_title)
        private val overview = view.findViewById<TextView>(R.id.tv_overview)
        private val rating = view.findViewById<TextView>(R.id.tv_rating)
        private val release = view.findViewById<TextView>(R.id.tv_release_date)

        @SuppressLint("SetTextI18n")
        fun bind(item: Movie) {
            title.text = item.title
            overview.text = item.overview
            rating.text = "Rating: ${item.vote_average}"
            release.text = item.release_date.toString()
        }
    }
}