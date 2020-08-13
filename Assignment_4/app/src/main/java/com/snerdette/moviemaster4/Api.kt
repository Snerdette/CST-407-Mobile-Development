package com.snerdette.moviemaster4

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "e4b524f368b538bcc3df7496161f39a9",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>


    @GET("movie/{id}/")
    fun getFavoriteMovies(
        @Query("api_key") apiKey: String = "e4b524f368b538bcc3df7496161f39a9",
        @Query("page") page: Int
    ): Call<GetMoviesResponse>
}