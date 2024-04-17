plugins {
    id("ooo.android.library")
}

android {
    namespace = "com.dangerfield.podawan.libraries.playback.internal"
}
dependencies {
    implementation(projects.libraries.playback)
    implementation(projects.libraries.common)
}
