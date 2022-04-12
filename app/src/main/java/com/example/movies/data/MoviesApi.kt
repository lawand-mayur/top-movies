package com.example.movies.data

import com.example.movies.BuildConfig
import retrofit2.Response
import retrofit2.http.GET

interface MoviesApi {
    @GET("movie?page=1&sort_by=popularity.desc&api_key="+BuildConfig.apiKey)
    suspend fun getMovies(): Response<MovieResponse>
}
