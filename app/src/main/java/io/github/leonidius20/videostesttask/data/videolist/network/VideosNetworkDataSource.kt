package io.github.leonidius20.videostesttask.data.videolist.network

import io.github.leonidius20.videostesttask.data.videolist.domain.Video
import io.github.leonidius20.videostesttask.data.videolist.network.api.VideosApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosNetworkDataSource @Inject constructor(
    private val api: VideosApi,
) {

    fun loadVideos(): Flow<NetworkResourceState<List<Video>>> {
        return flow {
            emit(NetworkResourceState.Loading)

            try {
                emit(
                    NetworkResourceState.Loaded(
                        api.getCategories().first().videos.map { it.toDomainObject() }
                    )
                )
            } catch (error: Throwable) {
                emit(NetworkResourceState.Error(error))
            }
        }
    }

}