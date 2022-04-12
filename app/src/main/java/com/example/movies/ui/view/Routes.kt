package com.example.movies.ui.view

sealed class Routes(val route:String) {
    object MovieList : Routes("MovieList")
    object MovieDetails : Routes("MovieDetails")
}