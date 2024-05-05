plugins {
    id("ooo.android.library")
}

podawan {
    daggerHilt()
    room()
    flowroutines()
}

android {
    namespace = "com.dangerfield.podawan.libraries.podcast.internal"
}
dependencies {
    implementation(libs.rss.parser)
    implementation(libs.coil)
    implementation(projects.features.playback)
    implementation(projects.libraries.podcast)
    implementation(projects.libraries.common)
    implementation(projects.libraries.podcast.storage)
    implementation(libs.immutable.collections)
}
