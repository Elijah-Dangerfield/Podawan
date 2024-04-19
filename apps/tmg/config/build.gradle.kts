plugins {
    id("ooo.android.library")
}

podawan {
    daggerHilt()
    compose()
}

android {
    namespace = "com.dangerfield.podawan.apps.tmg.config"
}
dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
}
