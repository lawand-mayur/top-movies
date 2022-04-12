package com.example.movies.di.modules

import com.example.movies.BuildConfig
import com.example.movies.common.Constants
import com.example.movies.data.MovieService
import com.example.movies.data.MoviesApi
import com.example.movies.domain.IDataRepository
import com.example.movies.domain.LoadMovieUseCase
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi {
          return retrofit.create(MoviesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit{
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(1,TimeUnit.MINUTES)
            .readTimeout(30,TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideMovieService(movieApi:MoviesApi): IDataRepository {
        return MovieService(movieApi)
    }

    @Provides
    fun provideLoadMovies(iDataRepository: IDataRepository): LoadMovieUseCase {
        return LoadMovieUseCase(iDataRepository)
    }
}