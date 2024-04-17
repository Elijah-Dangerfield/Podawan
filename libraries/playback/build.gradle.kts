plugins {
    id("ooo.android.library")
}

android {
    namespace = "com.dangerfield.podawan.libraries.playback"
}

dependencies {
    implementation(projects.libraries.common)
}
