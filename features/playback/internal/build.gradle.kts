plugins {
    id("ooo.android.feature")
}

podawan {
    daggerHilt()
    flowroutines()
    compose()
}
android {
    namespace = "com.dangerfield.podawan.features.playback.internal"
}
dependencies {
    implementation(projects.features.playback)
    implementation(projects.features.blockingError)

    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.ui.components)
    implementation(projects.libraries.podcast)
    implementation(libs.media3.common)
    implementation(libs.media3.session)
    implementation(libs.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.coil)

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation(project(":features:playlist")) // Needed MediaSessionCompat.Token
}
