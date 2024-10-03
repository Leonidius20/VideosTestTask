package io.github.leonidius20.videostesttask.features.videolist.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.github.leonidius20.videostesttask.data.videolist.domain.Video
import io.github.leonidius20.videostesttask.features.common.view.LoadingScreen
import io.github.leonidius20.videostesttask.features.videolist.model.VideoListUiState
import io.github.leonidius20.videostesttask.features.videolist.viewmodel.VideoListViewModel

@Composable
fun VideoListScreen(
    onOpenVideo: (Video) -> Unit,
) {
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
                onItemClick = { video ->
                    onOpenVideo(video)
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
                Text(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    text = video.subtitle,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(text = video.description)
            }
        },
        leadingContent = {
            AsyncImage(
                modifier = Modifier.width(64.dp),
                model = video.thumbnailUrl,
                contentDescription = null,
            )
        }
    )
}