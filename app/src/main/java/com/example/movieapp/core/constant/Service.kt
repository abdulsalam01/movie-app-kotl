package com.example.movieapp.core.constant

import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
import com.example.movieapp.model.Review
import com.example.movieapp.model.Video
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET(API.GENRES_LIST)
    fun getListGenres(@Query("api_key") api_key: String): Call<Genre.Response>

    @GET(API.MOVIE_LIST)
    fun getListMovie(@Query("api_key") api_key: String,
                     @Query("page") page: Int?,
                     @Query("with_genres") with_genres: Int): Call<Movie.Response>

    @GET(API.REVIEW_LIST)
    fun getReviewList(@Path("movie_id") movie_id: Int,
                      @Query("page") page: Int?,
                      @Query("api_key") api_key: String): Call<Review.Response>

    @GET(API.VIDEO_LIST)
    fun getVideoUrl(@Path("movie_id") movie_id: Int,
                    @Query("api_key") api_key: String): Call<Video.Response>
}