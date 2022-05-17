package com.theater

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Orchestrates playing of music and sound effects
 */
public class Orchestra(private val context: Context) {
    public val volume: Animatable<Float, AnimationVector1D> = Animatable(1f)
    public val player: MediaPlayer = MediaPlayer()

    public suspend fun playMusic(@RawRes resId: Int, block: (MediaPlayer.() -> Unit)? = null) {
        runCatching {
            withContext(Dispatchers.IO) {
                with(player) {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    val afd = context.resources.openRawResourceFd(resId)
                    setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    prepare()
                    start()
                }
            }
            block?.invoke(player)
        }.onFailure {
            Toast.makeText(context, R.string.error_playing_music, Toast.LENGTH_SHORT).show()
        }
    }

    public suspend fun decreaseVolume(
        spec: AnimationSpec<Float> = tween(@Suppress("MagicNumber") 2000),
    ) {
        volume.animateTo(0f, spec) { player.setVolume(value, value) }
    }
}
