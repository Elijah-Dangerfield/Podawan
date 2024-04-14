plugins {
    id("ooo.android.library")
}

podawan {
    flowroutines()
    daggerHilt()
    moshi()
}

android {
    namespace = "com.dangerfield.oddoneoout.libraries.config"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(libs.moshi)
}
