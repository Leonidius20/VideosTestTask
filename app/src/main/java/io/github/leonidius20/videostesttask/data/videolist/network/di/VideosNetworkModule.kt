package io.github.leonidius20.videostesttask.data.videolist.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leonidius20.videostesttask.data.videolist.network.api.VideosApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class VideosNetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/Leonidius20/VideosTestTask/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideVideosApi(
        retrofit: Retrofit
    ): VideosApi {
        return retrofit.create(VideosApi::class.java)
    }

}