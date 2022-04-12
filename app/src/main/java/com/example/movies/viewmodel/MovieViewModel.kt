package com.example.movies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.data.Movie
import com.example.movies.domain.LoadMovieUseCase
import com.example.movies.domain.MoviesEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieViewModel @Inject constructor(private val loadMovieUseCase: LoadMovieUseCase) : ViewModel() {
    private val _moviesEvent = MutableLiveData<MoviesEvent>(MoviesEvent.Loading)
    val moviesEvent: LiveData<MoviesEvent>
        get() = _moviesEvent

    private val _selectedMovie = MutableStateFlow<Movie?>(null)

    val selectedMovie: StateFlow<Movie?>
        get() = _selectedMovie

    fun fetchMovies() {
          _moviesEvent.postValue(MoviesEvent.Loading)
         viewModelScope.launch (Dispatchers.Main){
            _moviesEvent.postValue(loadMovieUseCase.getMovies())
        }
    }

    fun setSelectedMovie(movie: Movie) {
        _selectedMovie.value = movie
    }
}
