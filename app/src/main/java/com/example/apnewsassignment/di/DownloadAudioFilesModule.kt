package com.example.apnewsassignment.di

import com.example.apnewsassignment.data.data_source.AudioFilesApi
import com.example.apnewsassignment.data.repository.AudioFilesRepositoryImpl
import com.example.apnewsassignment.domain.repository.AudioFilesRepository
import com.example.apnewsassignment.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DownloadAudioFilesModule {

    @Provides
    @Singleton
    fun provideDownloadAudioFilesApi(retrofit: Retrofit): AudioFilesApi {
        return retrofit.create(AudioFilesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAudioFilesRepository(api: AudioFilesApi): AudioFilesRepository {
        return AudioFilesRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logging) // Add the interceptor to the OkHttpClient builder
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}