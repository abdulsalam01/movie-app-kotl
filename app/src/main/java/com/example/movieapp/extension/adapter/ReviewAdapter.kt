package com.example.movieapp.extension.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.core.constant.API
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Review
import com.squareup.picasso.Picasso

class ReviewAdapter(private val reviewList: List<Review>,
                    private val cellClickListener: ReviewAdapter.CellClickListener):
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    interface CellClickListener {
        fun onCellClickListener(data: Review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val row = inflater.inflate(R.layout.row_review, parent, false)

        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = this.reviewList.get(position)

        holder.bind(review)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(review)
        }
    }

    override fun getItemCount(): Int {
        return this.reviewList.size
    }

    // view-holder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val picasso = Picasso.get()

        private val avatar = view.findViewById<ImageView>(R.id.imgAvatar)
        private var author = view.findViewById<TextView>(R.id.tv_author)
        private var content = view.findViewById<TextView>(R.id.tv_content)

        fun bind(item: Review) {
            author.text = item.author
            content.text = item.content

            picasso.load(API.BASE_IMAGE + item.author__detail.avatar_path)
                .into(avatar)
        }
    }
}