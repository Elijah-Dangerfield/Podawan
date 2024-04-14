plugins {
    id("ooo.android.library")
}

podawan {
    flowroutines()
    daggerHilt()
}
android {
    namespace = "com.dangerfield.oddoneoout.libraries.common.internal"
}
dependencies {
    implementation(projects.libraries.common)
    implementation(libs.androidx.core)
}
