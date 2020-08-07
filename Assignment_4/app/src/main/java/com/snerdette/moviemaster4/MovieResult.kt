package com.snerdette.moviemaster4

import com.google.gson.annotations.SerializedName

//GET: /movies/search
data class MovieResult(
    @SerializedName("rating") val rating: Float,
    @SerializedName("id") val id: String,
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("title") val title: String,
    @SerializedName("image_url") val imageURL: String,
    @SerializedName("year") val year: Int
)
