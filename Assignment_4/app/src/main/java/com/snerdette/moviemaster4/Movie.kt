package com.snerdette.moviemaster4

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("vote_average") val rating: Float?,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("results") val movies: List<Movie>? = null,
    @SerializedName("favorite_movies") val favorites: List<Movie>? = null
)