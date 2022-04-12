package com.example.movies

import com.example.movies.component.ErrorState
import com.example.movies.component.ResultType
import com.example.movies.data.Movie
import com.example.movies.data.MovieResponse
import com.example.movies.data.MovieService
import com.example.movies.data.MoviesApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var movieService: MovieService

    private val movieApi = mockk<MoviesApi>()

    @Before
    fun setUp() {
        movieService = MovieService(movieApi)
    }

    @Test
    fun successTest(){
        val movieResponse = mockk<MovieResponse>()
        val response = mockk<Response<MovieResponse>>()
        every {
            response.body()
        } returns movieResponse
        every {
            response.isSuccessful
        } returns true

        coEvery {
            movieApi.getMovies()
        } returns response

        runTest {
            val result = movieService.getMovies()
            assert(result is ResultType.Success)
        }
    }

    @Test
    fun errorTest(){
        val response = mockk<Response<MovieResponse>>()
        every {
            response.isSuccessful
        } returns false

        coEvery {
            movieApi.getMovies()
        } returns response

        runTest {
            val result = movieService.getMovies()
            assert(result is ResultType.Error)
        }
    }
    @Test
    fun emptyBodyErrorTest(){
        val response = mockk<Response<MovieResponse>>()
        every {
            response.isSuccessful
        } returns true

        every {
            response.body()
        } returns null

        coEvery {
            movieApi.getMovies()
        } returns response

        runTest {
            val result = movieService.getMovies()
            assert(result is ResultType.Error)
        }
    }
    @Test
    fun networkFailureTest(){
        coEvery {
            movieApi.getMovies()
        } throws UnknownHostException()

        runTest {
            val result = movieService.getMovies()
            assert(result is ResultType.Error)
        }
    }
}