package io.github.leonidius20.videostesttask.data.videolist.network

sealed interface NetworkResourceState {

    data object Loading : NetworkResourceState

    data class Error(
        val error: Throwable
    ) : NetworkResourceState

    /**
     * successfully loaded or load was not initiated
     */
    data object NotLoading : NetworkResourceState

}