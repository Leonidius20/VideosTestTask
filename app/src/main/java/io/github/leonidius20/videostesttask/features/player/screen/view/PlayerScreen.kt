package io.github.leonidius20.videostesttask.features.player.screen.view

import android.content.ComponentName
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.MoreExecutors
import io.github.leonidius20.videostesttask.features.player.screen.viewmodel.PlayerViewModel
import io.github.leonidius20.videostesttask.features.player.service.PlaybackService

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

    val playerView = remember {
        PlayerView(context).apply {
            setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            // resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            setPlayer(player)

            /*player.addListener(object : Player.Listener {

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    mediaItem?.run {
                        viewModel.notifyPlayingVideoChangedTo(mediaItem.localConfiguration!!.uri.toString())
                    }
                }

            })*/
        }
    }

    // each time videos list changes, we re-set playlist

    if (videos.isNotEmpty()) {
        DisposableEffect(key1 = videos) {

            /*val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
            val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

            // todo: create it in a factory that accepts Context and turn this into coroutine

            controllerFuture.addListener({
                val controller = controllerFuture.get()


            }, MoreExecutors.directExecutor())*/

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


            onDispose {
                player.stop()
                player.release()
            }
        }
    }

    Scaffold { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = 16f / 9f),
                factory = {
                    playerView
                    /*PlayerView(context).apply {
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
                    }*/
                },
            )

            // this is the playlist
            LazyColumn {
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
