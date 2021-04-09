package com.example.movieapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Movie(val adult: Boolean, val backdrop_path: String,
                 val id: Int, val original_language: String,
                 val original_title: String, val overview: String,
                 val popularity: String, val poster_path: String,
                 val release_date: Date, val title: String,
                 val video: Boolean, val vote_average: Float, val vote_count: Int) {
    data class Response(val page: Int,
                        val total_pages: Int,
                        @SerializedName("results") val data: List<Movie>)
}