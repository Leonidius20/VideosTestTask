package io.github.leonidius20.videostesttask.data.videolist.db

import androidx.room.withTransaction
import io.github.leonidius20.videostesttask.data.common.db.VideosDatabase
import io.github.leonidius20.videostesttask.data.videolist.db.entity.VideoEntity
import io.github.leonidius20.videostesttask.data.videolist.domain.Video
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosDbDataSource @Inject constructor(
    private val db: VideosDatabase,
) {

    private val dao = db.videosDao()

    val videos = dao.getVideos().map { list ->
        list.map { it.toDomainObject() }
    }

    /**
     * @return true if cache is older than 30 minutes or empty
     */
    suspend fun isCacheStale(): Boolean {
        val cacheTimestamp = dao.cacheDate() ?: 0
        val currentTimestamp = System.currentTimeMillis()

        return (currentTimestamp - cacheTimestamp
                > TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES))
    }

    suspend fun clearAndInsertFreshData(data: List<Video>) {
        val cachedAt = System.currentTimeMillis()

        val dataMapped = data.map {
            VideoEntity(
                title = it.title,
                subtitle = it.subtitle,
                description = it.description,
                videoUrl = it.videoUrl,
                thumbnailUrl = it.thumbnailUrl,
                cachedAt = cachedAt,
            )
        }

        db.withTransaction {
            dao.clearAll()
            dao.insertAll(dataMapped)
        }
    }

    /*suspend fun isCacheEmpty(): Boolean {
        return dao.cacheDate() == null
    }*/

}