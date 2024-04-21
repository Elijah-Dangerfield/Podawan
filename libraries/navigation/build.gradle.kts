
plugins {
    id("ooo.android.library")
    id("kotlin-parcelize")
}

podawan {
    compose()
    flowroutines()
    daggerHilt()
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
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.browser.browser)
}
