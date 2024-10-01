package io.github.leonidius20.videostesttask.features.videolist.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.leonidius20.videostesttask.data.videolist.domain.Video
import io.github.leonidius20.videostesttask.features.videolist.model.VideoListUiState
import io.github.leonidius20.videostesttask.features.videolist.viewmodel.VideoListViewModel

@Composable
fun VideoListScreen() {
    val viewModel: VideoListViewModel = hiltViewModel()

    when (val state = viewModel.state.collectAsStateWithLifecycle().value) {
        is VideoListUiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }

        is VideoListUiState.Error -> {
            ErrorScreen(
                modifier = Modifier.fillMaxSize(),
                message = state.errorMessage,
            )
        }

        is VideoListUiState.Loaded -> {
            LoadedVideosListScreen(
                data = state.data,
                isRefreshInProgress = state.refreshInProgress,
                refreshErrorMessage = state.refreshErrorMessage,
            )
        }
    }
}

@Composable
private fun LoadedVideosListScreen(
    data: List<Video>,
    isRefreshInProgress: Boolean,
    refreshErrorMessage: String?,
) {
    Text(
        text = data.size.toString()
    )
}