package io.github.leonidius20.videostesttask.data.videolist.network.dto

import androidx.annotation.Keep

@Keep
data class VideosCategoryDto(
    val name: String,
    val videos: List<VideoDto>,
)
