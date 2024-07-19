plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
}
android {
    namespace = "com.dangerfield.podawan.features.episodedetails.internal"
}
dependencies {
    implementation(projects.features.episodeDetails)
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
}
