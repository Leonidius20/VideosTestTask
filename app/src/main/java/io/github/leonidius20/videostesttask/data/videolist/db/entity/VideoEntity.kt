package io.github.leonidius20.videostesttask.data.videolist.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.leonidius20.videostesttask.data.videolist.domain.Video

@Entity(tableName = "videos")
data class VideoEntity(
    val title: String,
    val subtitle: String,
    val description: String,
    @PrimaryKey val videoUrl: String,
    val thumbnailUrl: String,
    val cachedAt: Long, // timestamp
) {

    fun toDomainObject() = Video(
        title = title,
        subtitle = subtitle,
        description = description,
        videoUrl = videoUrl,
        thumbnailUrl = thumbnailUrl,
    )

}
