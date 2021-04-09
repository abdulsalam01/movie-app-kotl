package com.example.movieapp.ui.activity

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.core.constant.API
import com.example.movieapp.core.constant.Service
import com.example.movieapp.core.helper.BaseActivity
import com.example.movieapp.extension.adapter.ReviewAdapter
import com.example.movieapp.extension.singleton.ServiceManager
import com.example.movieapp.model.Review
import com.example.movieapp.model.Video
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailMovieActivity : AppCompatActivity(), BaseActivity {

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

    private lateinit var trailer: WebView
    private lateinit var imgPath: ImageView

    private var totalPage: Int = 0
    private var currentPage: Int = 1
    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        val picasso = Picasso.get()

        initView()

        this.title.text = titleMovie
        this.overview.text = overviewMovie
        this.popularity.text = "Popularity: ${popularityMovie}"
        this.releaseDate.text = releaseMovie

        picasso.load(API.BASE_IMAGE + backdropPath).into(this.imgPath)

        val gridLayoutManager = GridLayoutManager(this, 2)
        this.rvList.layoutManager = gridLayoutManager
        this.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val countItem = gridLayoutManager.itemCount
                val lastVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        loadReview(idMovie, currentPage)
        loadUrl(idMovie)

        // webview - player - yt
        trailer.getSettings().setJavaScriptEnabled(true)
        trailer.setWebChromeClient(WebChromeClient())
    }

    override fun initView() {
        this.idMovie = intent.getIntExtra("id_movie", 0)
        this.backdropPath = intent.getStringExtra("backdrop_path").toString()
        this.titleMovie = intent.getStringExtra("title").toString()
        this.overviewMovie = intent.getStringExtra("overview").toString()
        this.popularityMovie = intent.getStringExtra("popularity").toString()
        this.releaseMovie = intent.getStringExtra("release_date").toString()

        this.imgPath = findViewById(R.id.img_path)
        this.trailer = findViewById(R.id.webview)
        this.title = findViewById(R.id.tv_title)
        this.overview = findViewById(R.id.tv_overview)
        this.popularity = findViewById(R.id.tv_popularity)
        this.releaseDate = findViewById(R.id.tv_release_date)
        this.rvList = findViewById(R.id.rv_list)
        this.service = ServiceManager.getInstance()
    }

    private fun loadReview(id_movie: Int, page: Int) {
        this.service.getReviewList(id_movie, page, API.API_KEY).enqueue(object : Callback<Review.Response>,
            ReviewAdapter.CellClickListener {
            override fun onFailure(call: Call<Review.Response>, t: Throwable) {
                Snackbar.make(rvList, t.message.toString(), Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Review.Response>,
                response: Response<Review.Response>
            ) {
                reviewList = response.body()?.data!!
                totalPage = response.body()?.total_pages!!
                adapterReview = ReviewAdapter(reviewList, this)

                rvList.adapter = adapterReview
                isLoading = false
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
                trailer.loadUrl(API.YOUTUBE_TRAILER + data[0].key)
            }

        })
    }

}