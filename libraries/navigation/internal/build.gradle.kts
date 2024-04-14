plugins {
    id("ooo.android.library")
}

podawan {
    compose()
    flowroutines()
    daggerHilt()
}

android {
    namespace = "com.dangerfield.oddoneoout.libraries.navigation.internal"
}
dependencies {
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.common)
    implementation(projects.libraries.resources)
    implementation(projects.libraries.ui)
    implementation(projects.features.feed)
    implementation(projects.features.login)
    implementation(projects.features.search)
    implementation(projects.features.library)
    implementation(libs.browser)
    implementation(libs.androidx.material3.android)
}
