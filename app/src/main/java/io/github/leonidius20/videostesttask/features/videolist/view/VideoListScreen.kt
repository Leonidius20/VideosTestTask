package io.github.leonidius20.videostesttask.features.videolist.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
                onItemClick = {
                    // todo
                },
                onRefresh = { viewModel.refresh() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadedVideosListScreen(
    data: List<Video>,
    isRefreshInProgress: Boolean,
    refreshErrorMessage: String?,
    // onclick
    onItemClick: (Video) -> Unit,
    onRefresh: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding),
            isRefreshing = isRefreshInProgress,
            onRefresh = { onRefresh() },
        ) {

            LazyColumn {
                items(items = data, key = { video -> video.videoUrl }) { video ->
                    VideoListItem(
                        modifier = Modifier.clickable { onItemClick(video) },
                        video = video,
                    )
                }
            }

        }

    }

    LaunchedEffect(key1 = isRefreshInProgress, key2 = refreshErrorMessage) {
        val messageToShow =
            if (isRefreshInProgress)
                "Refreshing..."
            else refreshErrorMessage // could be null

        if (messageToShow != null) {
            snackbarHostState.showSnackbar(
                messageToShow
            )
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