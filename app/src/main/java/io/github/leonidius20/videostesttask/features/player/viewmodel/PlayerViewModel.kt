package io.github.leonidius20.videostesttask.features.player.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leonidius20.videostesttask.Destination
import io.github.leonidius20.videostesttask.data.videolist.VideosRepository
import io.github.leonidius20.videostesttask.features.player.model.PlayerUiState
import io.github.leonidius20.videostesttask.features.player.model.VideoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repo: VideosRepository,
) : ViewModel() {

    private val initialVideoUrl = savedStateHandle
        .toRoute<Destination.VideoPlayer>()
        .currentVideoUrl

    private val playingVideoUrl = MutableStateFlow(initialVideoUrl)

    val state = combine(repo.videos, playingVideoUrl) { list, playingUrl ->
        PlayerUiState(
            videos = list.mapTo(ArrayList(list.size)) { video ->
                VideoUiState(
                    url = video.videoUrl,
                    name = video.title,
                    isPlaying = video.videoUrl == playingUrl,
                )
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlayerUiState.defaultValue())

    /*fun onMovedToNextVideo() {
        val videos = state.value.videos
        val playingVideoIndex = videos.indexOfFirst { it.isPlaying }
        val nextIndex = playingVideoIndex + 1
        if (nextIndex < videos.size) {
            playingVideoUrl.value = videos[nextIndex].url
        }
    }

    fun onMovedToPrevVideo() {
        val videos = state.value.videos
        val playingVideoIndex = videos.indexOfFirst { it.isPlaying }
        val nextIndex = playingVideoIndex - 1
        if (nextIndex >= 0) {
            playingVideoUrl.value = videos[nextIndex].url
        }
    }*/

    fun onVideoChangedTo(url: String) {
        playingVideoUrl.value = url
    }
}