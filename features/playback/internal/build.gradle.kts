plugins {
    id("ooo.android.feature")
}

podawan {
    daggerHilt()
    flowroutines()
}
android {
    namespace = "com.dangerfield.podawan.features.playback.internal"
}
dependencies {
    implementation(projects.features.playback)
    implementation(projects.libraries.common)
    implementation(libs.media3.common)
    implementation(libs.media3.session)
    implementation(libs.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.coil)

    implementation("androidx.legacy:legacy-support-v4:1.0.0") // Needed MediaSessionCompat.Token
}
