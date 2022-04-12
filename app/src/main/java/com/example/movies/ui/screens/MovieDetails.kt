package com.example.movies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.movies.R
import com.example.movies.common.Constants
import com.example.movies.viewmodel.MovieViewModel

@Composable
fun MovieDetails(navHostController: NavHostController, movieViewModel: MovieViewModel) {
    val movie = movieViewModel.selectedMovie.collectAsState().value
    movie?.let {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black), contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.IMAGE_BASE_URL}${movie.backdrop_path}")
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.placeholder),
                    contentDescription = movie.title,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Text(
                    text = String.format(stringResource(id = R.string.name, it.title)),
                    modifier = Modifier.padding(20.dp),
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = it.overview, modifier = Modifier.padding(start = 20.dp),
                    color = Color.White
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    color = Color.LightGray,
                    thickness = 0.5.dp
                )

                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Language : " + it.original_language,
                        textAlign = TextAlign.Start,
                        color = Color.White,
                    )
                    Text(
                        text = "Release Date : " + it.release_date,
                        textAlign = TextAlign.End,
                        color = Color.White,
                    )
                }
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Average Votes : " + it.vote_average,
                        textAlign = TextAlign.End,
                        color = Color.White,

                        )
                    Text(
                        text = "Adult : " + if (it.adult) "Yes" else "No",
                        textAlign = TextAlign.End,
                        color = Color.White,

                        )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Popularity : " + it.popularity,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = "Total Vote : " + it.vote_count,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    color = Color.LightGray,
                    thickness = 0.5.dp
                )

            }
        }
    } ?: run { navHostController.navigateUp() }
}