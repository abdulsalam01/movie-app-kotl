package com.example.movieapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.core.constant.API
import com.example.movieapp.core.constant.Service
import com.example.movieapp.core.helper.BaseActivity
import com.example.movieapp.extension.adapter.MovieAdapter
import com.example.movieapp.extension.singleton.ServiceManager
import com.example.movieapp.model.Movie
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieActivity : AppCompatActivity(), BaseActivity {

    private lateinit var adapterMovie: MovieAdapter
    private lateinit var movieList: List<Movie>
    private lateinit var rvList: RecyclerView
    private lateinit var service: Service

    private var idGenre: Int = 0
    private var isLoading: Boolean = false
    private var currentPage: Int = 1
    private var totalPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        initView()

        val linearLayoutManager = LinearLayoutManager(this)
        this.rvList.layoutManager = linearLayoutManager
        this.rvList.addItemDecoration(
            DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL
            )
        )

        this.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition

                if (!isLoading && isLastPosition && currentPage <= totalPage) {
                    isLoading = true
                    currentPage++

                    loadMovie(currentPage, idGenre)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        loadMovie(currentPage, idGenre)
    }

    override fun initView() {
        this.idGenre = intent.getIntExtra("id_genre", 0)
        this.rvList = findViewById(R.id.rv_list)
        this.service = ServiceManager.getInstance()
    }

    private fun loadMovie(page: Int?, id_genre: Int) {
        this.service.getListMovie(API.API_KEY, page, id_genre).enqueue(object : Callback<Movie.Response>,
            MovieAdapter.CellClickListener {
            override fun onFailure(call: Call<Movie.Response>, t: Throwable) {
                Snackbar.make(rvList, t.message.toString(), Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Movie.Response>,
                response: Response<Movie.Response>
            ) {
                movieList = response.body()?.data!!
                totalPage = response.body()?.total_pages!!

                adapterMovie = MovieAdapter(movieList, this)

                rvList.adapter = adapterMovie
                isLoading = false
            }

            override fun onCellClickListener(data: Movie) {
                val i = Intent(baseContext, DetailMovieActivity::class.java)

                i.putExtra("id_movie", data.id)
                i.putExtra("title", data.title)
                i.putExtra("backdrop_path", data.backdrop_path)
                i.putExtra("overview", data.overview)
                i.putExtra("popularity", data.popularity)
                i.putExtra("release_date", data.release_date.toString())

                startActivity(i)
                finish()
            }
        })
    }
}