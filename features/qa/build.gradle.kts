plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
}
android {
    namespace = "com.dangerfield.oddoneoout.features.qa"
}
dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
}
