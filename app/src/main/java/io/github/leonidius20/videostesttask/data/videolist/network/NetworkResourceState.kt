package io.github.leonidius20.videostesttask.data.videolist.network

sealed interface NetworkResourceState<out T> {

    data object Loading : NetworkResourceState<Nothing>

    data class Error(
        val error: Throwable
    ) : NetworkResourceState<Nothing>

    data class Loaded<T>(
        val data: T
    ) : NetworkResourceState<T>

}