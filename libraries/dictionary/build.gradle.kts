plugins {
    id("ooo.android.library")
}

podawan {
    compose()
}
android {
    namespace = "com.dangerfield.oddoneoout.libraries.dictionary"
}

dependencies {
    implementation(projects.libraries.common)
}
