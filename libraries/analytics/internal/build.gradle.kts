plugins {
    id("ooo.android.library")
}

podawan {
    daggerHilt()
    firebase()
}

android {
    namespace = "com.dangerfield.oddoneoout.libraries.analytics.internal"
}
dependencies {
    implementation(projects.libraries.analytics)
    implementation(projects.libraries.common)
}
