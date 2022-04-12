package com.example.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movies.component.ErrorEvent
import com.example.movies.data.Movie
import com.example.movies.domain.LoadMovieUseCase
import com.example.movies.domain.MoviesEvent
import com.example.movies.viewmodel.MovieViewModel
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.IllegalArgumentException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var loadMovies: LoadMovieUseCase = mockk()

    private lateinit var movieViewModel: MovieViewModel

    @Before
    fun setup() {
        movieViewModel = MovieViewModel(loadMovies)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchMoviesSuccess() {
        val movieMockObj = mockk<Movie>()
        every {
            movieMockObj.title
        } returns "Avenger"

        val result = MoviesEvent.Success(listOf(movieMockObj))
        coEvery {
            loadMovies.getMovies()
        } returns result

        movieViewModel.fetchMovies()

        val movieEventData = movieViewModel.moviesEvent.value
        assert(movieEventData is MoviesEvent.Success)
        assert(
            (movieEventData as MoviesEvent.Success).movieList.first().title.equals(
                "Avenger", true
            )
        )
        assert((movieEventData).movieList.count() == 1)

    }

    @Test
    fun fetchMoviesEmptyResult() {
        val result = MoviesEvent.Success(listOf())
        coEvery {
            loadMovies.getMovies()
        } returns result
        movieViewModel.fetchMovies()
        assert(movieViewModel.moviesEvent.value is MoviesEvent.Success)
        assert((movieViewModel.moviesEvent.value as MoviesEvent.Success).movieList.isEmpty())

    }

    @Test
    fun fetchMoviesFailure() {
        val result =
            MoviesEvent.Error(ErrorEvent.APIError(IllegalArgumentException("Something went wrong")))
        coEvery {
            loadMovies.getMovies()
        } returns result

        movieViewModel.fetchMovies()
        assert(movieViewModel.moviesEvent.value is MoviesEvent.Error)
        ((movieViewModel.moviesEvent.value as MoviesEvent.Error).errorEvent as ErrorEvent.APIError).throwable.localizedMessage?.let {
            assert(it=="Something went wrong")
        }
    }

}

