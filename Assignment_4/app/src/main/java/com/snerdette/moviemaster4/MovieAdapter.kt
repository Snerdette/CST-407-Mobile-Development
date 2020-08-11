package com.snerdette.moviemaster4


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MovieAdapter(
    private var movies: MutableList<Movie>,
    private val onMovieClick: (movie: Movie) -> Unit,
    private val myLikedMovies : MutableList<LikeMovie>
): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val poster: ImageView = itemView.findViewById(R.id.item_movie_photo)
        private var movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private var movieRating: RatingBar = itemView.findViewById(R.id.movie_rating)
        private var movieReviewCount: TextView = itemView.findViewById(R.id.movie_review_count)

        fun bind(movie: Movie) {

            val toggleButton = itemView.findViewById<ToggleButton>(R.id.favoriteButton)
            toggleButton.setOnCheckedChangeListener { _, isChecked ->

                val user = FirebaseAuth.getInstance().currentUser
                val likeMovie = LikeMovie(null, user!!.uid, movie.id.toString())
                val database = FirebaseDatabase.getInstance()

                if (isChecked) {

                    var exists = false
                    myLikedMovies.forEach {
                        if (it.movieID == movie.id.toString()) {
                            exists = true
                        }
                    }

                    if (!exists) {
                        val newLikeReference = database.reference.child("Users").push().key
                        likeMovie.key = newLikeReference
                        database.reference.child("Users").child(newLikeReference.toString()).setValue(likeMovie)
                        myLikedMovies.add(likeMovie)
                    }


                }
                else {

                    myLikedMovies.forEach {
                        if (it.movieID == movie.id.toString()) {
                            likeMovie.key = it.key.toString()
                            database.reference.child("Users").child(it.key.toString()).removeValue()
                            myLikedMovies.remove(likeMovie)

                            myLikedMovies.removeIf { liked -> liked.key.equals(it.key.toString()) }
                        }
                    }
                }
            }

            var liked = false
            myLikedMovies?.forEach {
                if(movie.id.toString() == it.movieID) {
                    liked = true
                }
            }

            toggleButton.isChecked = liked

            if(movie.posterPath.contains("NULL")){
                // replace with default movie poster if url contains
                //val origImageURL = movie.defaultPoster
                Glide.with(itemView)
                    .load("https://www.reelviews.net/resources/img/default_poster.jpg")
                    .override(150, 100)
                    .transform(CenterCrop())
                    .into(poster)
            } else {
                val origImageURL = movie.posterPath
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                    .override(150, 100)
                    .transform(CenterCrop())
                    .into(poster)
            }



            movieTitle.text = movie.title
            movieRating.rating = movie.rating
            //movieReviewCount.text = "${movie.reviewCount} reviews"
            itemView.setOnClickListener {onMovieClick.invoke(movie)}
        }
    }
}