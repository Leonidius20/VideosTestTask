package io.github.leonidius20.videostesttask

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object VideosList : Destination

    @Serializable
    data class VideoPlayer(
        val currentVideoUrl: String,
    ) : Destination

}