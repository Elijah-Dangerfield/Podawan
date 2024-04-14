plugins {
    id("ooo.android.feature")
}

podawan {
    compose()
    daggerHilt()
}
android {
    namespace = "com.dangerfield.podawan.features.search.internal"
}
dependencies {
    implementation(projects.features.search)
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.libraries.ui)
}
