package com.example.movies.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movies.domain.LoadMovieUseCase
import javax.inject.Inject

class MoviesViewModelFactory @Inject constructor(private val loadMovieUseCase: LoadMovieUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(loadMovieUseCase) as T
    }
}