package com.example.movieapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.core.constant.API
import com.example.movieapp.core.constant.Service
import com.example.movieapp.extension.adapter.ReviewAdapter
import com.example.movieapp.extension.singleton.ServiceManager
import com.example.movieapp.model.Review
import com.example.movieapp.model.Video
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var adapterReview: ReviewAdapter
    private lateinit var reviewList: List<Review>
    private lateinit var rvList: RecyclerView
    private lateinit var service: Service

    private var idMovie: Int = 0
    private var titleMovie: String = ""
    private var overviewMovie: String = ""
    private var popularityMovie: String = ""
    private var releaseMovie: String = ""
    private var backdropPath: String = ""

    private lateinit var title: TextView
    private lateinit var overview: TextView
    private lateinit var popularity: TextView
    private lateinit var releaseDate: TextView

    private lateinit var trailer: VideoView
    private lateinit var imgPath: ImageView

    private var mediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val picasso = Picasso.get()

        this.idMovie = intent.getIntExtra("id_movie", 0)
        this.backdropPath = intent.getStringExtra("backdrop_path").toString()
        this.titleMovie = intent.getStringExtra("title").toString()
        this.overviewMovie = intent.getStringExtra("overview").toString()
        this.popularityMovie = intent.getStringExtra("popularity").toString()
        this.releaseMovie = intent.getStringExtra("release_date").toString()

        this.imgPath = findViewById(R.id.img_path)
        this.trailer = findViewById(R.id.v_trailer)
        this.title = findViewById(R.id.tv_title)
        this.overview = findViewById(R.id.tv_overview)
        this.popularity = findViewById(R.id.tv_popularity)
        this.releaseDate = findViewById(R.id.tv_release_date)
        this.rvList = findViewById(R.id.rv_list)
        this.service = ServiceManager.getInstance()

        this.title.text = titleMovie
        this.overview.text = overviewMovie
        this.popularity.text = "Popularity: ${popularityMovie}"
        this.releaseDate.text = releaseMovie

        picasso.load(API.BASE_IMAGE + backdropPath).into(this.imgPath)

        this.rvList.layoutManager = GridLayoutManager(this, 2)

        loadReview(idMovie)
        loadUrl(idMovie)
    }

    private fun loadReview(id_movie: Int) {
        this.service.getReviewList(id_movie, API.API_KEY).enqueue(object : Callback<Review.Response>,
            ReviewAdapter.CellClickListener {
            override fun onFailure(call: Call<Review.Response>, t: Throwable) {
                Snackbar.make(rvList, t.message.toString(), Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Review.Response>,
                response: Response<Review.Response>
            ) {
                reviewList = response.body()?.data!!
                adapterReview = ReviewAdapter(reviewList, this)

                rvList.adapter = adapterReview
            }

            override fun onCellClickListener(data: Review) {
            }
        })
    }

    private fun loadUrl(id_movie: Int) {
        this.service.getVideoUrl(id_movie, API.API_KEY).enqueue(object : Callback<Video.Response> {
            override fun onFailure(call: Call<Video.Response>, t: Throwable) {
                Snackbar.make(rvList, t.message.toString(), Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Video.Response>,
                response: Response<Video.Response>
            ) {
                val data = response.body()?.data!!

                trailer.setVideoPath(API.YOUTUBE_TRAILER + data[0].key)
                Log.d("HAHA", API.YOUTUBE_TRAILER + data[0].key);
                mediaController = MediaController(baseContext)
                mediaController?.setAnchorView(trailer)

                trailer.setMediaController(mediaController)
                trailer.start()
            }

        })
    }

}