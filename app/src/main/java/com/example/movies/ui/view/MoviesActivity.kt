package com.example.movies.ui.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movies.MovieApp
import com.example.movies.common.Constants
import com.example.movies.component.ErrorEvent
import com.example.movies.domain.MoviesEvent
import com.example.movies.ui.screens.*
import com.example.movies.viewmodel.MovieViewModel
import com.example.movies.viewmodel.MoviesViewModelFactory
import javax.inject.Inject

class MoviesActivity : ComponentActivity() {

    private lateinit var movieViewModel: MovieViewModel

    @Inject
    lateinit var moviesViewModelFactory: MoviesViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MovieApp.diComponent.inject(this)

        movieViewModel = ViewModelProvider(this, moviesViewModelFactory)[MovieViewModel::class.java]
        movieViewModel.fetchMovies()

        setContent {
            val navController = rememberNavController()
            NavigationSetup(navController, movieViewModel)
            when (val state = movieViewModel.moviesEvent.observeAsState().value) {
                is MoviesEvent.EmptyList -> EmptyView()
                is MoviesEvent.Loading -> ShimmerView(repeatCount = 6)
                is MoviesEvent.Error -> {
                    val message = when (state.errorEvent) {
                        is ErrorEvent.APIError -> state.errorEvent.throwable.message
                            ?: Constants.SOMETHING_WENT_WRONG
                        is ErrorEvent.ResponseError -> state.errorEvent.message
                    }
                    Toast.makeText(
                        this,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }

        }
    }
}

@Composable
fun NavigationSetup(navController: NavHostController, model: MovieViewModel) {
    NavHost(
        navController = navController,
        startDestination = Routes.MovieList.route,
    ) {
        composable(Routes.MovieList.route) {
            MovieList(navHostController = navController, model)
        }
        composable(Routes.MovieDetails.route) {
            MovieDetails(navHostController = navController, model)
        }
    }
}
