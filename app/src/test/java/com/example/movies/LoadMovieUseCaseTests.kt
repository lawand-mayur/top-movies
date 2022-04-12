package com.example.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies.component.ErrorEvent
import com.example.movies.component.ErrorState
import com.example.movies.component.ResultType
import com.example.movies.data.Movie
import com.example.movies.data.MovieResponse
import com.example.movies.domain.IDataRepository
import com.example.movies.domain.LoadMovieUseCase
import com.example.movies.domain.MoviesEvent
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoadMovieUseCaseTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var iDataRepository: IDataRepository = mockk()

    private lateinit var loadMovieUseCase: LoadMovieUseCase

    @Before
    fun setUp(){
        loadMovieUseCase = LoadMovieUseCase(iDataRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMoviesSuccessTest() {
        val movie = mockk<Movie>()
        every {
            movie.title
        } returns "Spider man"

        val movieResponse = mockk<MovieResponse>()
        every {
            movieResponse.results
        } returns listOf(movie)

        coEvery {
            iDataRepository.getMovies()
        } returns ResultType.Success(movieResponse)

        runTest {
            val response = loadMovieUseCase.getMovies()
            assert(response is MoviesEvent.Success)
            assert((response as MoviesEvent.Success).movieList.first().title == "Spider man")
        }
    }

    @Test
    fun getMoviesFailureNetworkErrorTest() {
        coEvery {
            iDataRepository.getMovies()
        } returns ResultType.Error(ErrorState.NetworkError("Network error"))

        runTest {
            val response = loadMovieUseCase.getMovies()
            assert( response is MoviesEvent.Error)
            val errorEvent = (response as MoviesEvent.Error).errorEvent
            assert(errorEvent is ErrorEvent.APIError)
            assert((errorEvent as ErrorEvent.APIError).throwable.localizedMessage == "Network error")
        }
    }
    @Test
    fun getMoviesFailureGenericErrorTest() {
        coEvery {
            iDataRepository.getMovies()
        } returns ResultType.Error(ErrorState.GenericError(Throwable("Generic error")))

        runTest {
            val response = loadMovieUseCase.getMovies()
            assert( response is MoviesEvent.Error)
            val errorEvent = (response as MoviesEvent.Error).errorEvent
            assert(errorEvent is ErrorEvent.APIError)
            assert((errorEvent as ErrorEvent.APIError).throwable.localizedMessage == "Generic error")
        }
    }
    @Test
    fun getMoviesFailureErrorResponseTest() {

        val errorResponse = mockk<ErrorState.ErrorResponse>()

        every { errorResponse.message  } returns "Error response"

        coEvery {
            iDataRepository.getMovies()
        } returns ResultType.Error(errorResponse)

        runTest {
            val response = loadMovieUseCase.getMovies()
            assert( response is MoviesEvent.Error)
            val errorEvent = (response as MoviesEvent.Error).errorEvent
            assert(errorEvent is ErrorEvent.ResponseError)
            assert((errorEvent as ErrorEvent.ResponseError).message == "Error response")
        }
    }

}

