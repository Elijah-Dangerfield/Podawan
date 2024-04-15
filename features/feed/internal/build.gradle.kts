plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
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
}
