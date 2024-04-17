plugins {
    id("ooo.android.library")
}

podawan {
    daggerHilt()
}

android {
    namespace = "com.dangerfield.podawan.libraries.podcast.internal"
}
dependencies {
    implementation(projects.libraries.podcast)
    implementation(projects.libraries.common)
    implementation(libs.rss.parser)
}
