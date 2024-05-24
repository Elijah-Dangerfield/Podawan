plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
    flowroutines()
}
android {
    namespace = "com.dangerfield.podawan.features.library.internal"
}
dependencies {
    implementation(projects.features.library)
    implementation(projects.features.playlist)
    implementation(projects.features.playlist.storage)
    implementation(projects.libraries.common)
    implementation(projects.libraries.podcast)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
}
