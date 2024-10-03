package io.github.leonidius20.videostesttask.features.videolist.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.leonidius20.videostesttask.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit,
) {
    Box(modifier) {
        Column(Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = message,
            )
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = { onRetry() }
            ) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}