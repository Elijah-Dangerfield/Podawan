plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
    flowroutines()
}
android {
    namespace = "com.dangerfield.podawan.features.playlist.internal"
}
dependencies {
    implementation(projects.features.playlist.storage)
    implementation(projects.features.playlist)
    implementation(projects.libraries.podcast)
    implementation(projects.libraries.podcast.storage)
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
    implementation(project(":features:playback"))
    implementation(project(":features:blockingError"))
    implementation(project(":features:feed"))
    implementation(project(":libraries:analytics"))
}
