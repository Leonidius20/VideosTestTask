package io.github.leonidius20.videostesttask.data.videolist.network.dto

import androidx.annotation.Keep
import io.github.leonidius20.videostesttask.data.videolist.domain.Video

@Keep
data class VideoDto(
    val title: String,
    val subtitle: String,
    val description: String,
    val sources: List<String>, // list of URLs
    private val thumb: String, // thumbnail relative URL e.g. images/BigBuckBunny.jpg.
) {

    val fullThumbnailUrl
        get() = "https://raw.githubusercontent.com/Leonidius20/VideosTestTask/refs/heads/master/$thumb"

    fun toDomainObject() = Video(
        title = title,
        subtitle = subtitle,
        description = description,
        videoUrl = sources.first(),
        thumbnailUrl = fullThumbnailUrl,
    )

}