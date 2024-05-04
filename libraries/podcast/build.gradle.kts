plugins {
    id("ooo.android.library")
}

podawan {
    flowroutines()
    compose()
}

android {
    namespace = "com.dangerfield.podawan.libraries.podcast"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.ui.components)
    implementation(projects.libraries.ui.preview)
}
