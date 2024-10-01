package io.github.leonidius20.videostesttask.features.videolist.view

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
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
    // onclick
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(items = data, key = { video -> video.videoUrl }) { video ->
                VideoListItem(
                    modifier = Modifier.clickable { /*todo*/ },
                    video = video,
                )
            }
        }
    }
}

@Composable
private fun VideoListItem(
    modifier: Modifier = Modifier,
    video: Video,
) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = video.title) },
        supportingContent = {
            Column {
                Text(text = video.subtitle)
                Text(text = video.description)
            }
        }
    )
}