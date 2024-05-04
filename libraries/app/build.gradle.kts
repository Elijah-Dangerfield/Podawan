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
    api(projects.libraries.common)
    api(projects.libraries.navigation)
    api(projects.libraries.navigation.internal)
    api(projects.features.consent)
    api(projects.features.inAppMessaging)
    api(projects.libraries.session)
    api(projects.libraries.ui)
    api(projects.libraries.ui.components)
    api(projects.libraries.ui.preview)

    api(projects.features.forcedUpdate)
    api(projects.features.blockingError)
    api(projects.features.auth)
    api(projects.libraries.flowroutines)
    api(projects.libraries.network.internal)
    api(projects.features.inAppMessaging.internal)
    api(projects.libraries.network)
    api(projects.libraries.logging)
    api(projects.libraries.session.internal)

    api(libs.androidx.material3.android)
    api(libs.androidx.core.splashscreen)
    api(libs.androidx.lifecycle.process)

    api(libs.coil)

    api(projects.features.feed)
    api(projects.features.auth)
    api(projects.features.search)
    api(projects.features.library)
    api(projects.features.playback)
    api(projects.features.playback.internal)

    // in app update
    api(libs.android.play.app.update)
    api(libs.android.play.app.update.ktx)

    api(projects.libraries.analytics)
    api(projects.libraries.config)
    api(projects.libraries.dictionary.internal)
    api(projects.libraries.podcast.internal)
    api(projects.libraries.podcast)
}
