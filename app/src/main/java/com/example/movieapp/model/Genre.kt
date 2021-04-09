package com.example.movieapp.model

import com.google.gson.annotations.SerializedName

data class Genre(val id: Int, val name: String) {
    data class Response(@SerializedName("genres")
                        val data: List<Genre>)
}