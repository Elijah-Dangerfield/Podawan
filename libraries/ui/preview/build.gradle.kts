plugins {
    id("ooo.android.library")
}

podawan {
    compose()
}
android {
    namespace = "com.dangerfield.podawan.libraries.ui.preview"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.resources)

    // Right now transitive dependencies need to be exposed to be used public functions/classes
    api(projects.apps.theBenAndEmilShow.config)
    api(projects.apps.tmg.config)
    api(projects.apps.stuffYouShouldKnow.config)
}
