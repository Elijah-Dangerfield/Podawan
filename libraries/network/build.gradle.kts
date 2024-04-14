plugins {
    id("ooo.android.library")
}

podawan {
    flowroutines()
    firebase()
}

android {
    namespace = "com.dangerfield.oddoneoout.libraries.network"
}

dependencies {
    implementation(projects.libraries.common)
}
