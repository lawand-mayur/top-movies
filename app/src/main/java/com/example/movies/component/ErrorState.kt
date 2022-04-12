package com.example.movies.component

sealed class ErrorState {
    data class GenericError(val exception: Throwable) : ErrorState()
    data class NetworkError(val message: String) : ErrorState()
    data class ErrorResponse(val code: Int, val message: String) : ErrorState()
}
