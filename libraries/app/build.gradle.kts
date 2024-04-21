plugins {
    id("ooo.android.library")
}

podawan {
    compose()
    firebase()
    daggerHilt()
    moshi()
}
android {
    namespace = "com.dangerfield.podawan.libraries.app"
}

dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.navigation)
    implementation(projects.features.consent)
    implementation(projects.features.inAppMessaging)
    implementation(projects.libraries.session)

    implementation(projects.libraries.ui)
    implementation(projects.libraries.ui.components)
    implementation(projects.libraries.ui.preview)

    implementation(project(":features:forcedUpdate"))
    implementation(project(":features:blockingError"))
    implementation(project(":features:auth"))
    implementation(project(":libraries:flowroutines"))
    implementation(project(":libraries:network:internal"))
    implementation(project(":features:inAppMessaging:internal"))
    implementation(project(":libraries:network"))
    implementation(project(":libraries:logging"))
    implementation(project(":libraries:session:internal"))

    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.process)

    implementation(projects.features.feed)
    implementation(projects.features.auth)
    implementation(projects.features.search)
    implementation(projects.features.library)

    // in app update
    implementation(libs.android.play.app.update)
    implementation(libs.android.play.app.update.ktx)

    implementation(project(":libraries:analytics"))
    implementation(project(":libraries:config"))
    implementation(project(":libraries:dictionary:internal"))
}
