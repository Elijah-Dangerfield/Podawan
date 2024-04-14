plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
}

android {
    namespace = "com.dangerfield.podawan.features.library"
}
dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.navigation)
}
