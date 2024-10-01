package io.github.leonidius20.videostesttask.data.videolist.domain

data class Video(
    val title: String,
    val subtitle: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
)