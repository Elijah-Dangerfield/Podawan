plugins {
    id("ooo.android.library")
}

podawan {
    flowroutines()
}

android {
    namespace = "com.dangerfield.podawan.libraries.podcast"
}

dependencies {
    implementation(projects.libraries.common)
}
