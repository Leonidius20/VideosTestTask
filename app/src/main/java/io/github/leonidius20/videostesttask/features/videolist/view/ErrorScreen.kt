package io.github.leonidius20.videostesttask.features.videolist.view

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String,
) {
    Box(modifier) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = message
        )
    }
}