package io.github.leonidius20.videostesttask.data.common.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.leonidius20.videostesttask.data.common.db.VideosDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): VideosDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            VideosDatabase::class.java,
            "videos.db"
        ).build()
    }

}