plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
    flowroutines()
}
android {
    namespace = "com.dangerfield.podawan.features.feed.internal"
}
dependencies {
    implementation(projects.features.feed)
    implementation(projects.features.blockingError)
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.resources)
    implementation(projects.libraries.podcast)
}
