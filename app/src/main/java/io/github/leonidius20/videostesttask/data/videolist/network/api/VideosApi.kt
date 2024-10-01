package io.github.leonidius20.videostesttask.data.videolist.network.api

import io.github.leonidius20.videostesttask.data.videolist.network.dto.VideosCategoryDto
import retrofit2.http.GET

interface VideosApi {

    @GET("categories")
    suspend fun getCategories(): List<VideosCategoryDto>

}