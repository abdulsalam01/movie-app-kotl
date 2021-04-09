package com.example.movieapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.core.constant.API
import com.example.movieapp.core.constant.Service
import com.example.movieapp.core.helper.BaseActivity
import com.example.movieapp.extension.adapter.GenreAdapter
import com.example.movieapp.extension.adapter.MovieAdapter
import com.example.movieapp.extension.singleton.ServiceManager
import com.example.movieapp.model.Genre
import com.example.movieapp.model.Movie
import com.example.movieapp.ui.activity.MovieActivity
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), BaseActivity {

    private lateinit var adapterGenre: GenreAdapter
    private lateinit var genreList: List<Genre>
    private lateinit var rvList: RecyclerView
    private lateinit var service: Service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        this.rvList.layoutManager = LinearLayoutManager(this)
        this.rvList.addItemDecoration(
            DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL
            )
        )

        loadGenre()
    }



    private fun loadGenre() {
        this.service.getListGenres(API.API_KEY).enqueue(object : Callback<Genre.Response>,
            GenreAdapter.CellClickListener {
            override fun onFailure(call: Call<Genre.Response>, t: Throwable) {
                Snackbar.make(rvList, t.message.toString(), Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<Genre.Response>,
                response: Response<Genre.Response>
            ) {
                genreList = response.body()?.data!!
                adapterGenre = GenreAdapter(genreList, this)

                rvList.adapter = adapterGenre
            }

            override fun onCellClickListener(data: Genre) {
                val i = Intent(baseContext, MovieActivity::class.java)

                i.putExtra("name", data.name)
                i.putExtra("id_genre", data.id)

                startActivity(i)
            }

        })
    }

    override fun initView() {
        this.rvList = findViewById(R.id.rv_list)
        this.service = ServiceManager.getInstance()
    }
}