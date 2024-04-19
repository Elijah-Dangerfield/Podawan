import com.spyfall.podawan.util.buildConfigField

plugins {
    id("ooo.android.application")
    id("com.google.firebase.crashlytics")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
}

podawan {
    compose()
    flowroutines()
    daggerHilt()
    firebase()
}

android {

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

        setProperty("archivesBaseName", "app-v$versionName($versionCode)")
    }

    buildTypes {
        val release = getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }

        /*
        qa builds should match release builds as much as possible
        some notes differences (not exhaustive):
        - test ad unit ids
        - debug log tree is planted
        - app suffix id is qa (so that actual release and debug can also be installed)
         */
        create("qa") {
            // Enable all the optimizations from release build through initWith(release).
            initWith(release)
            matchingFallbacks.add("release")
            isDebuggable = true
            buildConfigField("IS_QA", true)
            // Debug key signing is available to everyone.
            signingConfig = signingConfigs.getByName("debug")
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            isMinifyEnabled = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.ext)
    implementation(libs.androidx.lifecycle.vm)
    implementation(libs.androidx.core)
    implementation(libs.kotlin.std)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.moshi)

    // dagger
    implementation(project.libs.dagger)
    implementation(project.libs.dagger.hilt.android)
    implementation(project(":features:inAppMessaging"))
    kapt(project.libs.dagger.hilt.android.compiler)
    
    // in app update
    implementation(libs.android.play.app.update)
    implementation(libs.android.play.app.update.ktx)

    // firebase libraries
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)

    //  that sweet sweet kotlin coroutines library
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.navigation.compose)

    // data consent
    implementation(libs.user.messaging.platform)

    implementation(projects.apps.theBenAndEmilShow.config)

    /**
     * Project Modules
     * Both internal and external should be included here to make sure all objects
     * are added to the dependency graph
     */
    implementation(projects.features.forcedUpdate)
    implementation(projects.features.forcedUpdate.internal)
    implementation(projects.features.settings)
    implementation(projects.features.settings.internal)
    implementation(projects.features.blockingError)
    implementation(projects.features.blockingError.internal)
    implementation(projects.libraries.storage)
    implementation(projects.libraries.config)
    implementation(projects.libraries.config.internal)
    implementation(projects.libraries.config.internal)
    implementation(projects.libraries.logging)
    implementation(projects.libraries.common)
    implementation(projects.libraries.common.internal)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.ui.components)
    implementation(projects.libraries.session)
    implementation(projects.libraries.session.internal)
    implementation(projects.features.qa)
    implementation(projects.features.qa.internal)
	implementation(projects.libraries.resources)
	implementation(projects.libraries.resources.internal)
	implementation(projects.libraries.navigation)
	implementation(projects.libraries.navigation.internal)
	implementation(projects.libraries.network)
	implementation(projects.libraries.network.internal)
	implementation(projects.libraries.storage.internal)
	implementation(projects.libraries.analytics)
	implementation(projects.libraries.analytics.internal)
	implementation(projects.libraries.dictionary)
	implementation(projects.libraries.dictionary.internal)
	implementation(projects.features.consent)
	implementation(projects.features.consent.internal)
	implementation(projects.features.inAppMessaging)
	implementation(projects.features.inAppMessaging.internal)
	implementation(projects.libraries.session.storage)
	implementation(projects.features.auth)
	implementation(projects.features.auth.internal)
	implementation(projects.features.feed)
	implementation(projects.features.feed.internal)
	implementation(projects.features.search)
	implementation(projects.features.search.internal)
	implementation(projects.features.library)
	implementation(projects.features.library.internal)
	implementation(projects.libraries.podcast)
	implementation(projects.libraries.podcast.internal)
	implementation(projects.libraries.playback)
	implementation(projects.libraries.playback.internal)
	implementation(projects.libraries.app)
    // STOP PROJECT MODULES (keep this line at the end of the project modules, used by ./create_module)

    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.arch.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
}