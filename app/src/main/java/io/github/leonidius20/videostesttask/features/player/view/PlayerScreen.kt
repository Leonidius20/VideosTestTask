package io.github.leonidius20.videostesttask.features.player.view

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import io.github.leonidius20.videostesttask.features.player.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen() {
    val viewModel: PlayerViewModel = hiltViewModel()

    val videos = viewModel.videos.collectAsStateWithLifecycle().value

    val playingVideoUrl = viewModel.playingVideoUrl.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context)
            .build()
    }

    // each time videos list changes, we re-set playlist
    LaunchedEffect(key1 = videos) {
        if (videos.isNotEmpty()) {
            player.setMediaItems(
                videos.map { video ->
                    MediaItem.fromUri(video.url)
                }
            )
            player.prepare()

            // seeking to the video that should be playing

            player.seekTo(
                videos.indexOfFirst { it.url == playingVideoUrl.value }, 0
            )


            player.play()
        }
    }

    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AndroidView(
                modifier = Modifier.fillMaxWidth().aspectRatio(ratio = 16f / 9f),
                factory = {
                    PlayerView(context).apply {
                        setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                        // resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        setPlayer(player)

                        player.addListener(object : Player.Listener {

                            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                                mediaItem?.run {
                                    viewModel.notifyPlayingVideoChangedTo(mediaItem.localConfiguration!!.uri.toString())
                                }
                            }

                        })
                    }
                },
            )


            // this is the playlist
            LazyColumn(modifier = Modifier) {
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
