package com.example.movies.domain

import com.example.movies.component.ErrorEvent
import com.example.movies.component.ErrorState
import com.example.movies.component.ResultType
import com.example.movies.data.Movie
import javax.inject.Inject

open class LoadMovieUseCase @Inject constructor(private val iDataRepository: IDataRepository) {

    suspend fun getMovies(): MoviesEvent {
        return when (val response = iDataRepository.getMovies()) {
            is ResultType.Success -> handleMovieResponse(response.value.results)
            is ResultType.Error -> handleErrorResponse(response.state)
        }
    }

    private fun handleErrorResponse(errorState: ErrorState) = when (errorState) {
        is ErrorState.ErrorResponse -> MoviesEvent.Error(ErrorEvent.ResponseError(errorState.message))
        is ErrorState.GenericError -> MoviesEvent.Error(ErrorEvent.APIError(errorState.exception))
        is ErrorState.NetworkError -> MoviesEvent.Error(ErrorEvent.APIError(Throwable(errorState.message)))
    }

    private fun handleMovieResponse(movieList: List<Movie>?) = if (movieList.isNullOrEmpty()) {
        MoviesEvent.EmptyList
    } else {
        val sortedList =
            if (movieList.size >= 10) {
                movieList.sortedByDescending { it.vote_average }.subList(0, 10)
            } else {
                movieList.sortedByDescending { it.vote_average }
            }
        MoviesEvent.Success(sortedList)

    }
}

