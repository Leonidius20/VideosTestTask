package io.github.leonidius20.videostesttask.features.player.screen.view

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import io.github.leonidius20.videostesttask.features.player.screen.model.PlayerUiState
import io.github.leonidius20.videostesttask.features.player.screen.viewmodel.PlayerViewModel
import io.github.leonidius20.videostesttask.features.common.view.LoadingScreen

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen() {
    val viewModel: PlayerViewModel = hiltViewModel()

    val state = viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state.value) {
        is PlayerUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
        is PlayerUiState.Loaded -> {

            val player = currentState.player
            val videos = currentState.videos

            val playingVideoUrl = viewModel.playingVideoUrl.collectAsStateWithLifecycle()

            val context = LocalContext.current

            val playerView = remember {
                PlayerView(context).apply {
                    setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                }
            }

            LaunchedEffect(key1 = currentState) {
                playerView.player = player
                player.play()
            }

            Scaffold { innerPadding ->
                Column(Modifier.padding(innerPadding)) {
                    val isLandscape =
                        LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE

                    val playerModifier =
                        if (isLandscape)
                            Modifier.fillMaxSize()
                        else Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 16f / 9f)

                    AndroidView(
                        modifier = playerModifier,
                        factory = {
                            playerView
                        },
                    )

                    // this is the playlist
                    if (!isLandscape) {
                        val listState = rememberLazyListState()

                        LazyColumn(state = listState) {
                            items(
                                count = videos.size,
                                key = { videos[it].url }) { videoIndex ->
                                val video = videos[videoIndex]
                                PlaylistItem(
                                    title = video.name,
                                    isSelected = video.url == playingVideoUrl.value
                                )
                            }
                        }

                        LaunchedEffect(key1 = playingVideoUrl.value) {
                            val playingItemIndex = videos.indexOfFirst {
                                it.url == playingVideoUrl.value
                            }
                            listState.scrollToItem(playingItemIndex)
                        }
                    }

                }
            }

        }
    }

}

@Composable
private fun PlaylistItem(
    title: String,
    isSelected: Boolean,
) {
    ListItem(
        headlineContent = {
            Text(text = title)
        },
        leadingContent = {
            if (isSelected) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Playing video indicator")
            }
        }
    )
}
