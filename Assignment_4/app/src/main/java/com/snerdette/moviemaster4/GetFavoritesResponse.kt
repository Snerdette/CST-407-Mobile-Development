package com.snerdette.moviemaster4

import com.google.gson.annotations.SerializedName

//GET: /movies/search
data class GetFavoritesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val myLikedMovies: List<Movie>,
    @SerializedName("total_pages") val pages: Int
)