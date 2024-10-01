package io.github.leonidius20.videostesttask.data.videolist.network

import io.github.leonidius20.videostesttask.data.videolist.domain.Video
import io.github.leonidius20.videostesttask.data.videolist.network.api.VideosApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosNetworkDataSource @Inject constructor(
    private val api: VideosApi,
) {

    suspend fun loadVideos(): List<Video> = withContext(Dispatchers.IO) {
        return@withContext api.getCategories()
            .first()
            .videos
            .map { it.toDomainObject() }
    }

}