plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
}
android {
    namespace = "com.dangerfield.podawan.features.library.internal"
}
dependencies {
    implementation(projects.features.library)
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
}
