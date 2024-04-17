plugins {
    id("ooo.android.library")
}

android {
    namespace = "com.dangerfield.podawan.libraries.app.internal"
}
dependencies {
    implementation(projects.libraries.app)
    implementation(projects.libraries.common)
}
