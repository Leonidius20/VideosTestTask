package io.github.leonidius20.videostesttask.features.player.model

data class PlayerUiState(
    val videos: ArrayList<VideoUiState>,
) {

    companion object {
        fun defaultValue() = PlayerUiState(ArrayList())
    }

}

data class VideoUiState(
    val url: String,
   // val isPlaying: Boolean,
    val name: String,
)
