package io.github.leonidius20.videostesttask.features.videolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leonidius20.videostesttask.data.videolist.VideosRepository
import io.github.leonidius20.videostesttask.data.videolist.network.NetworkResourceState
import io.github.leonidius20.videostesttask.features.videolist.model.VideoListUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val repository: VideosRepository,
) : ViewModel() {

    val state = combine(repository.videos, repository.loadingState) { videosList, loadingState ->
        if (videosList.isEmpty()) {
            if (loadingState is NetworkResourceState.Error) {
                VideoListUiState.Error(
                    loadingState.error.message ?: "Error loading data. Check your connection"
                )
            } else {
                VideoListUiState.Loading
            }
        } else {
            val refreshError = (loadingState as? NetworkResourceState.Error)?.error
            val refreshInProgress = loadingState is NetworkResourceState.Loading

            VideoListUiState.Loaded(
                data = videosList,
                refreshErrorMessage = refreshError?.message,
                refreshInProgress = refreshInProgress,
            )
        }

    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        VideoListUiState.Loading)

    init {
        viewModelScope.launch {
            repository.refreshIfNeeded()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.forceRefresh()
        }
    }

}