package com.example.movies

import android.app.Application
import com.example.movies.di.component.ApiComponent
import com.example.movies.di.component.DaggerApiComponent

class MovieApp : Application() {
    companion object {
        lateinit var diComponent: ApiComponent
    }
    override fun onCreate() {
        super.onCreate()
        diComponent = DaggerApiComponent.builder().build()
    }
}