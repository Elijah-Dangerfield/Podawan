plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
    flowroutines()
}
android {
    namespace = "com.dangerfield.podawan.features.auth.internal"
}
dependencies {
    implementation(projects.features.auth)
    implementation(projects.features.feed)
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.session)
    implementation(projects.libraries.analytics)
}
