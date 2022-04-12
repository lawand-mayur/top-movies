package com.example.movies.di.component

import com.example.movies.di.modules.ApiModule
import com.example.movies.ui.view.MoviesActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(moviesActivity : MoviesActivity)
}