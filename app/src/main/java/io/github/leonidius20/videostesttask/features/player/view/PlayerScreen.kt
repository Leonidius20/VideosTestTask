package io.github.leonidius20.videostesttask.features.player.view

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import io.github.leonidius20.videostesttask.features.player.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen() {
    val viewModel: PlayerViewModel = hiltViewModel()

    val state = viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val player = remember {
            ExoPlayer.Builder(context)
                .build()
    }

    // each time videos list changes, we re-set playlist
    LaunchedEffect(key1 = state.value) {
        val videos = state.value.videos

        player.setMediaItems(
            videos.map { video ->
                MediaItem.fromUri(video.url)
            }
        )
        player.prepare()

        // seeking to the video that should be playing
        if (videos.isNotEmpty()) {
            player.seekTo(
                videos.indexOfFirst { it.isPlaying }, 0
            )
        }

        player.play()
    }

    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = {
                    PlayerView(context).apply {
                        setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                        setPlayer(player)

                        /*player.addListener(object : Player.Listener {

                            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                                mediaItem?.run {
                                    viewModel.onVideoChangedTo(mediaItem.mediaId)
                                }
                            }

                        })*/

                        //player.prepare()

                        // player.play()
                    }
                },
            )


            // this is the playlist
            LazyColumn {
                items(count = state.value.videos.size, key = { state.value.videos[it].url }) { videoIndex ->
                    val video = state.value.videos[videoIndex]
                    PlaylistItem(
                        title = video.name,
                        // todo: replace with state saved in viewmodel and updated by listener, do not use the combined state
                        isSelected = player.currentMediaItemIndex == videoIndex
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
        modifier = if (isSelected) Modifier.background(MaterialTheme.colorScheme.primaryContainer) else Modifier,
        headlineContent = {
        Text(text = title)
    })
}
