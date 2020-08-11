package com.snerdette.moviemaster4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_movies.*


class FragmentFavorites : Fragment() {

    private var movie: RecyclerView? = null
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieLayoutMgr: LinearLayoutManager

    private var movieOffset = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    private fun onMoviesFetched(myLikedMovies: List<Movie>) {
        //Log.d("Movie:", "$movies")
        movieAdapter.appendFavoriteMovies(myLikedMovies as MutableList<LikeMovie>)
        attachMovieResultsOnScrollListener()
    }

    private fun getMovie() {
        MoviesRepository.getPopularMovies(
            movieOffset,
            ::onMoviesFetched,
            ::onError
        )
    }

    private fun onError() {
        Toast.makeText(context, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun attachMovieResultsOnScrollListener() {
        movie?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //Total number of movies inside MovieAdapter
                val totalItemCount = movieLayoutMgr.itemCount

                //Current number of child views attached to RecyclerView
                val visibleItemCount = movieLayoutMgr.findLastVisibleItemPosition() - movieLayoutMgr.findFirstVisibleItemPosition()

                //Position of leftmost visible item in list
                val firstVisibleItem = movieLayoutMgr.findFirstVisibleItemPosition()


                if(firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    Log.d("Main: visibleItemCount:", "$visibleItemCount")
                    Log.d("Main: totalItemCount:", "$totalItemCount")
                    //Disable scroll listener, increment moviesResultsLimit and call function
                    movie!!.removeOnScrollListener(this)
                    movieOffset += 20
                    getMovie()
                }
            }
        })
    }


    private fun showMovieDetails(movie: Movie) {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_fragment_name.text = "Favorite Movies"

        movie = activity?.findViewById(R.id.list_movies)
        movieLayoutMgr = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        movie?.layoutManager = movieLayoutMgr
        val mySnapshot = ArrayList<LikeMovie>()
        movieAdapter = MovieAdapter(mutableListOf(), { movie -> showMovieDetails(movie) }, mySnapshot)
        movie?.adapter = movieAdapter
        MoviesRepository.getPopularMovies(
            movieOffset,
            onSuccess = ::onMoviesFetched,
            onError = ::onError
        )
        getMovie()
    }

}