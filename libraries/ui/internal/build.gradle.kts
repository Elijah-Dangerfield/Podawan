
plugins {
    id("ooo.android.library")
}

podawan {
    compose()
}

android {
    namespace = "com.dangerfield.oddoneoout.ui.internal"
}
dependencies {
    implementation(projects.libraries.common)
}
