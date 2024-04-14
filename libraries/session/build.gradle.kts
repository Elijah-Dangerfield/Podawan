plugins {
    id("ooo.android.library")
}

podawan {
    flowroutines()
    moshi()
    room()
}
android {
    namespace = "com.dangerfield.oddoneoout.libraries.session"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
    implementation(project.libs.kotlinx.serialization.json)
}
