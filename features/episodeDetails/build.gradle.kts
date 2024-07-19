plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
    flowroutines()
}

android {
    namespace = "com.dangerfield.podawan.features.episodedetails"
}
dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.navigation)
    implementation(project(":features:blockingError"))
    implementation(project(":features:playlist"))
    implementation(project(":libraries:podcast"))
    implementation(project(":features:playback"))
}
