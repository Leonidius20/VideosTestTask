package io.github.leonidius20.videostesttask.features.player.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class PlayerFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun create(): Player = suspendCoroutine {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture.addListener({
            try {
                it.resume(controllerFuture.get())
            } catch (e: Throwable) {
                it.resumeWithException(e)
            }
        }, MoreExecutors.directExecutor())
    }

}