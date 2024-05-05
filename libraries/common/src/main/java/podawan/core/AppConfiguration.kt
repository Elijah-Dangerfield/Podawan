package podawan.core

import android.graphics.drawable.Drawable
import javax.annotation.concurrent.Immutable

@Immutable
interface AppConfiguration {
    val rssFeedLink: String
    val appName: String
    val appIcon: Drawable
}
