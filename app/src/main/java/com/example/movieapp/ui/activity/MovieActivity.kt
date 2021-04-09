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
import com.example.movieapp.extension.adapter.MovieAdapter
import com.example.movieapp.extension.singleton.ServiceManager
import com.example.movieapp.model.Movie
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieActivity : AppCompatActivity() {

    private lateinit var adapterMovie: MovieAdapter
    private lateinit var movieList: List<Movie>
    private lateinit var rvList: RecyclerView
    private lateinit var service: Service

    private var idGenre: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        this.idGenre = intent.getIntExtra("id_genre", 0)
        this.rvList = findViewById(R.id.rv_list)
        this.service = ServiceManager.getInstance()

        this.rvList.layoutManager = LinearLayoutManager(this)
        this.rvList.addItemDecoration(
            DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL
            )
        )

        loadMovie(1, idGenre)
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
                adapterMovie = MovieAdapter(movieList, this)

                rvList.adapter = adapterMovie
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