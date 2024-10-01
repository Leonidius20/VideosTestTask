package io.github.leonidius20.videostesttask.features.videolist.model

import io.github.leonidius20.videostesttask.data.videolist.domain.Video

sealed interface VideoListUiState {

    data object Loading

    /**
     * nothing is loaded, nothing is cached
     */
    data class Error(
        val errorMessage: String,
    )

    /**
     * something is available to show, but it is possible that it is
     * stale data from cache, and refresh failed
     */
    data class Loaded(
        val data: List<Video>,
        val refreshErrorMessage: String? = null,
    )

}