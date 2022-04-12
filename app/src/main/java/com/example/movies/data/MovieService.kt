package com.example.movies.data

import com.example.movies.component.safeApiCall
import com.example.movies.domain.IDataRepository
import javax.inject.Inject

class MovieService @Inject constructor(private val api: MoviesApi): IDataRepository {
    override suspend fun getMovies() = safeApiCall { api.getMovies() }
}