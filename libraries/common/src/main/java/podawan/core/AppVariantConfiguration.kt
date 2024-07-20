package podawan.core

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.font.FontFamily

/**
 * Represents the configuration for a specific variant of the app.
 * These are values not in the app config or controlled remotely but bundled with the app
 * and used to distinguish differences between different apps. This class is expected to grow
 * quite a bit.
 */
@Immutable
interface AppVariantConfiguration {
    val rssFeedLink: String
    val appName: String
    val appIcon: Drawable
    val hasSearchTab: Boolean
    val fontConfig: FontConfig
}

sealed class FontConfig {
    object Default: FontConfig()

    class Custom(
        val headerFamily: FontFamily,
        val bodyFamily: FontFamily,
        val labelFamily: FontFamily,
        val displayFamily: FontFamily
    ): FontConfig()
}
