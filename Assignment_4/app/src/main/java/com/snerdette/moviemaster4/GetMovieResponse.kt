package com.snerdette.moviemaster4

import com.google.gson.annotations.SerializedName

//GET: /movies/search
data class GetMovieResponse(
    @SerializedName("movies") val movies: List<MovieResult>
)
