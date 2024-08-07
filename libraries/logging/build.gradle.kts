plugins {
    id("ooo.android.library")
}

podawan {
    firebase()
}
android {
    namespace = "com.dangerfield.oddoneoout.libraries.logging"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
}
