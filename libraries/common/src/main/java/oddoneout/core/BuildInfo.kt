package podawan.core

import android.os.Build

/**
 * Typed wrapper around BuildConfig which can be injected anywhere to
 * determine the current[versionCode], and [versionName] and other needed items
 */
data class BuildInfo(
    val versionCode: Int,
    val versionName: String,
    val packageName: String,
    val buildType: BuildType,
    val app: App,
    val deviceName: String = getDeviceName()
) {
    val playStorePackageName = packageName
        .split(".")
        .filter { it != "debug" }
        .joinToString(".")

    val isDebug: Boolean = buildType == BuildType.DEBUG
    val isQA: Boolean = buildType == BuildType.QA
    val isRelease: Boolean = buildType == BuildType.RELEASE
}

enum class BuildType {
    DEBUG,
    RELEASE,
    QA
}

enum class App {
    TMG,
    TheBenAndEmilShow,
    StuffYouShouldKnow
}

fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val modelContainsManufacturer = model.contains(manufacturer, ignoreCase = true)
    return if (modelContainsManufacturer) {
        model
    } else {
        "$manufacturer $model"
    }
}
