plugins {
    id("ooo.android.library")
    id("androidx.room")
}

val schemasDir = layout.projectDirectory.dir("schemas")

room {
    schemaDirectory(schemasDir.toString())
}

podawan {
    daggerHilt()
    moshi()
    flowroutines()
    room()
}

android {
    namespace = "com.dangerfield.oddoneoout.libraries.storage.internal"
}

dependencies {
    implementation(projects.libraries.storage)
    implementation(projects.libraries.common)

    implementation(libs.autoDagger.androidx.room)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.libraries.session.storage)
    implementation(projects.features.playlist.storage)
    implementation(projects.libraries.podcast.storage)

}
