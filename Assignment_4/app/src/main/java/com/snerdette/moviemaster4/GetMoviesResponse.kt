package com.snerdette.moviemaster4

import com.google.gson.annotations.SerializedName

//GET: /movies/search
data class GetMoviesResponse(
    //@SerializedName("movies") val movies: List<MovieResult>,
    @SerializedName("page") val page: Int,
    @SerializedName("results") val movies: List<Movie>,
    @SerializedName("total_pages") val pages: Int
)
