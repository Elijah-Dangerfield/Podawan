plugins {
    id("ooo.android.library")
}

podawan {
    room()
}

android {
    namespace = "com.dangerfield.oddoneoout.libraries.session.storage"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.session)
}
