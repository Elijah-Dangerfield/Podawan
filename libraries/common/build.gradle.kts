plugins {
    id("ooo.android.library")
    kotlin("plugin.parcelize")
    id("org.jetbrains.kotlin.android")
}

podawan {
    moshi()
    firebase()
    compose()
}

dependencies {
    implementation(libs.kotlin.std)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.core)
}

android {
    namespace = "podawan.core.common"
}
