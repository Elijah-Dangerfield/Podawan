plugins {
    id("ooo.android.library")
}

podawan {
    room()
}

android {
    namespace = "com.dangerfield.podawan.features.playlist.storage"
}
dependencies {
    implementation(projects.libraries.common)
}
