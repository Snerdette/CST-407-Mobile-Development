package com.snerdette.moviemaster4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_movies.*


class FragmentMovies : Fragment() {

    private var movieResults: RecyclerView? = null
    private lateinit var movieResultsAdapter: MovieAdapter
    private lateinit var movieResultsLayoutMgr: LinearLayoutManager

    private var movieResultsOffset = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    private fun onMoviesFetched(movies: List<MovieResult>) {
        Log.d("MovieActivity:", "$movies")
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
        Toast.makeText(context, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    private fun attachMovieResultsOnScrollListener() {
        movieResults?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //Total number of movies inside MovieAdapter
                val totalItemCount = movieResultsLayoutMgr.itemCount

                //Current number of child views attached to RecyclerView
                val visibleItemCount = movieResultsLayoutMgr.findLastVisibleItemPosition() - movieResultsLayoutMgr.findFirstVisibleItemPosition()

                //Position of leftmost visible item in list
                val firstVisibleItem = movieResultsLayoutMgr.findFirstVisibleItemPosition()


                if(firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    Log.d("Main: visibleItemCount:", "$visibleItemCount")
                    Log.d("Main: totalItemCount:", "$totalItemCount")
                    //Disable scroll listener, increment moviesResultsLimit and call function
                    movieResults!!.removeOnScrollListener(this)
                    movieResultsOffset += 20
                    getMovieResults()
                }
            }
        })
    }

    private fun showMovieDetails(movie: MovieResult) {
        val intent = Intent(context, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.imageURL)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_REVIEWCOUNT, "${movie.reviewCount} reviews")
        startActivity(intent)
    }


     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_fragment_name.text = "Movies"

         movieResults = activity?.findViewById(R.id.list_movies)
         movieResultsLayoutMgr = LinearLayoutManager(
             context,
             LinearLayoutManager.VERTICAL,
             false
         )
         movieResults?.layoutManager = movieResultsLayoutMgr
         val mySnapshot = ArrayList<LikeMovie>()
         movieResultsAdapter = MovieAdapter(mutableListOf(), { movie -> showMovieDetails(movie) }, mySnapshot)
         movieResults?.adapter = movieResultsAdapter
         MoviesRepository.getPopularMoviesResults(
             movieResultsOffset,
             onSuccess = ::onMoviesFetched,
             onError = ::onError
         )
         getMovieResults()
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