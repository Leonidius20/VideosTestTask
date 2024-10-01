package io.github.leonidius20.videostesttask.data.videolist.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.leonidius20.videostesttask.data.videolist.db.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideosDao {

    @Query("select * from videos")
    fun getVideos(): Flow<List<VideoEntity>>

    /**
     * @return null if the cache is empty
     */
    @Query("select cachedAt from videos limit 1")
    suspend fun cacheDate(): Long?

    @Query("delete from videos")
    suspend fun clearAll()

    @Insert
    suspend fun insertAll(data: List<VideoEntity>)

}