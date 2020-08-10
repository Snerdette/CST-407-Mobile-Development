package com.snerdette.moviemaster4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_movies.*


class FragmentMovies : FragmentActivity() {

    private lateinit var movieResults: RecyclerView
    private lateinit var movieResultsAdapter: MovieAdapter
    private lateinit var movieResultsLayoutMgr: LinearLayoutManager

    private var movieResultsOffset = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
     fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    private fun onMoviesFetched(movies: List<MovieResult>) {
        Log.d("MainActivity:", "$movies")
        movieResultsAdapter.appendMovies(movies)
        attachMovieResultsOnScrollListener()
    }


    private fun getMovieResults() {
        MoviesRepository.getPopularMoviesResults(
            movieResultsOffset,
            ::onMoviesFetched,
            ::onError
        )
    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun attachMovieResultsOnScrollListener() {
        movieResults.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //Total number of businesses inside BusinessAdapter
                val totalItemCount = movieResultsLayoutMgr.itemCount

                //Current number of child views attached to RecyclerView
                val visibleItemCount = movieResultsLayoutMgr.findLastVisibleItemPosition() - movieResultsLayoutMgr.findFirstVisibleItemPosition()

                //Position of leftmost visible item in list
                val firstVisibleItem = movieResultsLayoutMgr.findFirstVisibleItemPosition()


                if(firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    Log.d("Main: visibleItemCount:", "$visibleItemCount")
                    Log.d("Main: totalItemCount:", "$totalItemCount")
                    //Disable scroll listener, increment businessResultsLimit and call function
                    movieResults.removeOnScrollListener(this)
                    movieResultsOffset += 20
                    getMovieResults()
                }
            }
        })
    }

    private fun showMovieDetails(movie: MovieResult) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.imageURL)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_REVIEWCOUNT, "${movie.reviewCount} reviews")
        startActivity(intent)
    }


     fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_fragment_name.text = "Movies"

        movieResults = findViewById(R.id.list_movies)
        movieResultsLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        movieResults.layoutManager = movieResultsLayoutMgr

        val mySnapshot = ArrayList<LikeMovie>()

        movieResultsAdapter = MovieAdapter(mutableListOf(), { movie -> showMovieDetails(movie) }, mySnapshot)
        movieResults.adapter = movieResultsAdapter

        MoviesRepository.getPopularMoviesResults(
            movieResultsOffset,
            onSuccess = ::onMoviesFetched,
            onError = ::onError
        )
        getMovieResults()
        // Add CardView init here!!!
    }

    /*
    *
    * override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    *
    *
    * companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentLists.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLists().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    * */

}