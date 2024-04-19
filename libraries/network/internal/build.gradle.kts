plugins {
    id("ooo.android.library")
}

podawan {
    flowroutines()
    daggerHilt()
    firebase()
    compose()
}

android {
    namespace = "com.dangerfield.oddoneoout.libraries.network.internal"
}
dependencies {
    implementation(projects.libraries.ui)
    implementation(projects.libraries.ui.components)
    implementation(projects.libraries.network)
    implementation(projects.libraries.common)
}
