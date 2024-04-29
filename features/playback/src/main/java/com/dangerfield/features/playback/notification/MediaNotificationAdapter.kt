package com.dangerfield.features.playback.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.runBlocking

@UnstableApi
class MediaNotificationAdapter(
    private val context: Context,
    private val pendingIntent: PendingIntent?
) : PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence =
        player.mediaMetadata.albumTitle ?: ""

    override fun createCurrentContentIntent(player: Player): PendingIntent? =
        pendingIntent

    override fun getCurrentContentText(player: Player): CharSequence =
        player.mediaMetadata.displayTitle ?: ""

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {

        val request = ImageRequest.Builder(context)
            .data(player.mediaMetadata.artworkUri)
            .build()

        val drawable = runBlocking {
            context.imageLoader.execute(request).drawable
        }

        return drawable?.toBitmap()?.also {
            callback.onBitmap(it)
        }
    }
}
