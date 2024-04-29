plugins {
    id("ooo.android.library")
}

podawan {
    room()
}

android {
    namespace = "com.dangerfield.podawan.libraries.podcast.storage"
}

dependencies {
    implementation(projects.libraries.common)
}
