package io.github.leonidius20.videostesttask.features.videolist.model

import io.github.leonidius20.videostesttask.data.videolist.domain.Video

sealed interface VideoListUiState {

    data object Loading : VideoListUiState

    /**
     * nothing is loaded, nothing is cached
     */
    data class Error(
        val errorMessage: String,
    ) : VideoListUiState

    /**
     * something is available to show, but it is possible that it is
     * stale data from cache, and refresh failed
     */
    data class Loaded(
        val data: List<Video>,

        /**
         * if cached data is shown, and refresh was requested but failed
         */
        val refreshErrorMessage: String? = null,

        /**
         * if cached data is shown, but refresh is happening in the background
         */
        val refreshInProgress: Boolean = false,
    ) : VideoListUiState

}