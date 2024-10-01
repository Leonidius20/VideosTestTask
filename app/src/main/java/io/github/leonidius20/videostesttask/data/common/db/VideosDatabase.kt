package io.github.leonidius20.videostesttask.data.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.leonidius20.videostesttask.data.videolist.db.dao.VideosDao
import io.github.leonidius20.videostesttask.data.videolist.db.entity.VideoEntity

@Database(
    entities = [
        VideoEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class VideosDatabase : RoomDatabase() {

    abstract fun videosDao(): VideosDao

}