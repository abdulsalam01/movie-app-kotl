package com.example.movieapp.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class Review(val author: String,
                  @SerializedName("author_details")
                  val author__detail: AuthorDetails,
                  val content: String,
                  val id: String, val url: String) {

    data class AuthorDetails(val username: String,
                             val avatar_path: String,
                             val rating: Float) {}

    data class Response(val id: Int, val page: Int,
                        @SerializedName("results") val data: List<Review>)
}