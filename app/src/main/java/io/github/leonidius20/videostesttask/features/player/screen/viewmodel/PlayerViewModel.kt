package io.github.leonidius20.videostesttask.features.player.screen.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.leonidius20.videostesttask.Destination
import io.github.leonidius20.videostesttask.data.videolist.VideosRepository
import io.github.leonidius20.videostesttask.features.player.screen.model.PlayerUiState
import io.github.leonidius20.videostesttask.features.player.screen.model.VideoUiState
import io.github.leonidius20.videostesttask.features.player.service.PlayerFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repo: VideosRepository,
    private val playerFactory: PlayerFactory,
) : ViewModel() {

    private val _state = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val state = _state.asStateFlow()

    private val initialVideoUrl = savedStateHandle
        .toRoute<Destination.VideoPlayer>()
        .currentVideoUrl

    val playingVideoUrl = MutableStateFlow(initialVideoUrl)

    init {
        viewModelScope.launch {

            val videosDomainObjects = repo.videos.first()

            val videos = videosDomainObjects
                .mapTo(ArrayList(videosDomainObjects.size)) { video ->
                    VideoUiState(
                        url = video.videoUrl,
                        name = video.title,
                    )
                }

            val player = getPlayer(videos, initialVideoUrl)

            _state.value = PlayerUiState.Loaded(
                videos = videos,
                player = player
            )

        }
    }

    /*val videos: StateFlow<ArrayList<VideoUiState>> = repo.videos.map { list ->
        list.mapTo(ArrayList(list.size)) { video ->
            VideoUiState(
                url = video.videoUrl,
                name = video.title,
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ArrayList())
    */

    /*  val state = combine(repo.videos, playingVideoUrl) { list, playingUrl ->
          PlayerUiState(
              videos = list.mapTo(ArrayList(list.size)) { video ->
                  VideoUiState(
                      url = video.videoUrl,
                      name = video.title,
                      isPlaying = video.videoUrl == playingUrl,
                  )
              }
          )
      }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlayerUiState.defaultValue())
  */
    private fun notifyPlayingVideoChangedTo(url: String) {
        playingVideoUrl.value = url
    }

    //private var player: Player? = null

    private suspend fun getPlayer(
        videos: List<VideoUiState>,
        currentVideoUrl: String,
    ): Player {
        return playerFactory.create().apply {
            addListener(object : Player.Listener {

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    mediaItem?.run {
                        notifyPlayingVideoChangedTo(mediaItem.localConfiguration!!.uri.toString())
                    }
                }

            })

            setMediaItems(
                videos.map { video ->
                    MediaItem.fromUri(video.url)
                }
            )
            prepare()
            seekTo(
                videos.indexOfFirst { it.url == currentVideoUrl }, 0
            )
            //play()
        }//.also { player = it }
    }

    override fun onCleared() {
        (state.value as? PlayerUiState.Loaded)?.player?.apply {
            stop()
            release()
        }
    }

}