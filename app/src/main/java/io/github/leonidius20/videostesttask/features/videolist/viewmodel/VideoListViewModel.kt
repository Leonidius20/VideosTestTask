package io.github.leonidius20.videostesttask.features.videolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leonidius20.videostesttask.data.videolist.VideosRepository
import io.github.leonidius20.videostesttask.data.videolist.network.NetworkResourceState
import io.github.leonidius20.videostesttask.data.videolist.network.VideosNetworkDataSource
import io.github.leonidius20.videostesttask.features.videolist.model.VideoListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val repository: VideosRepository,
    private val networkDataSource: VideosNetworkDataSource, // todo remove
) : ViewModel() {

    val state = networkDataSource.loadVideos()
        .map { netResState ->
            when (netResState) {
                is NetworkResourceState.Loading -> VideoListUiState.Loading
                is NetworkResourceState.Error -> {
                    VideoListUiState.Error(errorMessage = netResState.error.message ?: "")
                }

                is NetworkResourceState.Loaded -> {
                    VideoListUiState.Loaded(
                        data = netResState.data
                    )
                }
            }
        }.stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            VideoListUiState.Loading)

    // val state = combine

    init {
        // repo.refresh if needed
    }


    fun refresh() {
        // repo.force refresh
        // todo: pull to refresh?
    }

}