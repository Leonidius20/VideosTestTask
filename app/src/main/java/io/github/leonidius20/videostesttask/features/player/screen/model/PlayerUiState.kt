package io.github.leonidius20.videostesttask.features.player.screen.model

import androidx.media3.common.Player

sealed interface PlayerUiState {

    data object Loading : PlayerUiState

    data class Loaded(
        val videos: ArrayList<VideoUiState>,
        val player: Player,
        // todo: currently playing video
    ) : PlayerUiState

}

data class VideoUiState(
    val url: String,
    val name: String,
)
