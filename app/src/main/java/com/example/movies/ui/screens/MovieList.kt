package com.example.movies.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movies.R
import com.example.movies.common.Constants
import com.example.movies.component.ErrorEvent
import com.example.movies.data.Movie
import com.example.movies.domain.MoviesEvent
import com.example.movies.ui.theme.MyApplicationTheme
import com.example.movies.ui.view.Routes
import com.example.movies.viewmodel.MovieViewModel
import kotlinx.coroutines.launch

@Composable
fun MovieList(navHostController: NavHostController, movieViewModel: MovieViewModel) {
    MyApplicationTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            color = MaterialTheme.colors.background,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                when (val state = movieViewModel.moviesEvent.observeAsState().value) {
                    is MoviesEvent.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(start = 20.dp, end = 20.dp)

                        ) {
                            items(state.movieList.count()) { index ->
                                LoadMovie(state.movieList[index], navHostController, movieViewModel)
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

}

@Composable
fun LoadMovie(movie: Movie, navHostController: NavHostController, movieViewModel: MovieViewModel) {

    val scope = rememberCoroutineScope()
    Column(Modifier
        .padding(top = 40.dp)
        .clickable(
            onClick = {
                scope.launch {
                    movieViewModel.setSelectedMovie(movie)
                    navHostController.navigate(Routes.MovieDetails.route)
                }
            }
        )) {
        ConstraintLayout(
            modifier = Modifier
                .wrapContentSize()
        ) {
            val (moviePoster, ratings) = createRefs()
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("${Constants.IMAGE_BASE_URL}${movie.poster_path}")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = movie.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .height(220.dp)
                    .border(
                        border = BorderStroke(2.dp, Color.White),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .constrainAs(moviePoster) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = "Ratings : " + movie.vote_average,
                color = Color.White,
                modifier = Modifier
                    .constrainAs(ratings) {
                        top.linkTo(moviePoster.top)
                        end.linkTo(moviePoster.end)
                    }
                    .padding(top = 10.dp, end = 10.dp)
                    .background(Color(0x5111100F))
                    .wrapContentWidth()
            )

        }

    }
}