package io.github.leonidius20.videostesttask.data.videolist

import io.github.leonidius20.videostesttask.data.videolist.db.VideosDbDataSource
import io.github.leonidius20.videostesttask.data.videolist.network.NetworkResourceState
import io.github.leonidius20.videostesttask.data.videolist.network.VideosNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosRepository @Inject constructor(
    private val dbDataSource: VideosDbDataSource,
    private val networkDataSource: VideosNetworkDataSource,
) {

    val videos = dbDataSource.videos

    private val _loadingState = MutableStateFlow<NetworkResourceState>(
        NetworkResourceState.NotLoading
    )

    val loadingState = _loadingState.asStateFlow()

    suspend fun refreshIfNeeded() = withContext(Dispatchers.IO) {
        if (dbDataSource.isCacheStale()) {
            forceRefresh()
        }
    }

    suspend fun forceRefresh() = withContext(Dispatchers.IO) {
        _loadingState.value = NetworkResourceState.Loading
        try {
            val freshData = networkDataSource.loadVideos()
            dbDataSource.clearAndInsertFreshData(freshData)
            _loadingState.value = NetworkResourceState.NotLoading
        } catch (e: Throwable) {
            _loadingState.value = NetworkResourceState.Error(e)
        }
    }

}