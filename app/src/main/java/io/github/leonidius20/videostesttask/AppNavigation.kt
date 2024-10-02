package io.github.leonidius20.videostesttask

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.leonidius20.videostesttask.features.player.view.PlayerScreen
import io.github.leonidius20.videostesttask.features.videolist.view.VideoListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destination.VideosList
    ) {
        composable<Destination.VideosList> {
            VideoListScreen(
                onOpenVideo = { video ->
                    navController.navigate(Destination.VideoPlayer(video.videoUrl))
                }
            )
        }
        composable<Destination.VideoPlayer> {
            PlayerScreen()
        }
    }
}