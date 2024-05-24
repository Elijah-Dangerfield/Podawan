package podawan.core

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Immutable

@Immutable
interface AppConfiguration {
    val rssFeedLink: String
    val appName: String
    val appIcon: Drawable
}
