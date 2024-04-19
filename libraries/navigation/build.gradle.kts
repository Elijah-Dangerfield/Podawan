
plugins {
    id("ooo.android.library")
}

podawan {
    compose()
}
android {
    namespace = "com.dangerfield.oddoneoout.libraries.navigation"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.ui.components)

    api(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
}
