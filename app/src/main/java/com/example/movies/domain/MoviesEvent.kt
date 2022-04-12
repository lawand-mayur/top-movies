package com.example.movies.domain

import com.example.movies.component.ErrorEvent
import com.example.movies.data.Movie

sealed class MoviesEvent {
    data class Success(val movieList: List<Movie>) : MoviesEvent()
    object EmptyList : MoviesEvent()
    object Loading : MoviesEvent()
    data class Error(val errorEvent: ErrorEvent) : MoviesEvent()
}