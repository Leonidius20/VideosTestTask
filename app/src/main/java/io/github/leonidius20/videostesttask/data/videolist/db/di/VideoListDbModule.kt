package io.github.leonidius20.videostesttask.data.videolist.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.leonidius20.videostesttask.data.common.db.VideosDatabase
import io.github.leonidius20.videostesttask.data.videolist.db.dao.VideosDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class VideoListDbModule {

    @Provides
    @Singleton
    fun provideVideoDao(db: VideosDatabase): VideosDao = db.videosDao()

}