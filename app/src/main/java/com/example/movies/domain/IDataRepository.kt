package com.example.movies.domain

import com.example.movies.component.ResultType
import com.example.movies.data.MovieResponse

interface IDataRepository {
    suspend fun getMovies(): ResultType<MovieResponse>
}