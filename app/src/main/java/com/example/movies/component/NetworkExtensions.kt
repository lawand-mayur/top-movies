package com.example.movies.component

import com.example.movies.common.Constants
import retrofit2.Response
import java.net.UnknownHostException

@SuppressWarnings("TooGenericExceptionCaught")
suspend fun <T : Any> safeApiCall(apiCall: suspend () -> Response<T>): ResultType<T> {
    return try {
        apiCall().toResult()
    } catch (throwable: Throwable) {
        when(throwable)
        {
            is UnknownHostException -> ResultType.Error(ErrorState.NetworkError(Constants.NO_NETWORK_CONNECTION))
            else -> {
                ResultType.Error(ErrorState.GenericError(throwable))
            }
        }
    }
}

fun <T : Any> Response<T>.toResult(): ResultType<T> {
    return when {
        isSuccessful -> body()?.let {
            ResultType.Success(it)
        } ?: ResultType.Error(ErrorState.GenericError(IllegalStateException("Empty body in response.")))
        else -> ResultType.Error(ErrorState.ErrorResponse(this.code(),this.message()))
    }
}
