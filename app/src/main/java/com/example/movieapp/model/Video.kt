package com.example.movieapp.model

import com.google.gson.annotations.SerializedName

data class Video(val id: String, val key: String) {
    data class Response(val id: Int,
                        @SerializedName("results") val data: List<Video>)
}