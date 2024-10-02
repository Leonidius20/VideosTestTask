package io.github.leonidius20.videostesttask.features.player.view

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
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
            .apply {
                setMediaSource(ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(
                    MediaItem.fromUri(viewModel.initialVideoUrl)
                ))
            }
    }

    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = {
                    PlayerView(context).apply {
                        setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                        setPlayer(player)


                        //player.setMediaItems(state.value.videos.map { video ->
                        //    MediaItem.fromUri(video.url)
                            /*MediaItem.Builder()
                                .setUri(video.url)
                                .setMediaId(video.url)
                                .setMediaMetadata(
                                    MediaMetadata.Builder()
                                        .setDisplayTitle(video.name)
                                        .build()
                                )
                                .build()*/
                        //})

                        player.addListener(object : Player.Listener {

                            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                                mediaItem?.run {
                                    viewModel.onVideoChangedTo(mediaItem.mediaId)
                                }
                            }

                        })

                        player.prepare()

                        player.play()
                    }
                }
            )
        }
    }

}