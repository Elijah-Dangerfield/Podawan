package com.spyfall.podawan.util

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
    isFeature: Boolean,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
        }

        dependencies {
            val bom = libs.androidx.compose.bom

            if (isFeature) {
                add("implementation", getModule("libraries:ui"))
                add("implementation", getModule("libraries:ui:components"))
                add("implementation", getModule("libraries:ui:preview"))
            }

            add("implementation", platform(bom))
            add("implementation", libs.androidx.compose.runtime)
            add("implementation", libs.androidx.lifecycle.runtimeCompose)
            add("implementation", libs.coil)
            add("implementation", libs.androidx.lifecycle)
            add("implementation", libs.hilt.navigation.compose)
            add("implementation", libs.androidx.navigation.compose)
            add("androidTestImplementation", platform(bom))
            add("implementation",libs.immutable.collections)

            // Add ComponentActivity to debug manifest
            add("debugImplementation", libs.androidx.compose.ui.testManifest)
            add("debugImplementation", libs.androidx.compose.ui.tooling)
            add("implementation", libs.androidx.ui.tooling.preview.android)
            add("implementation", libs.androidx.customview.poolingcontainer)
            add("implementation", libs.androidx.compose.ui.tooling.preview)

            add("testImplementation", libs.robolectric)

            add("implementation", libs.androidx.compose.foundation)
            add("implementation", libs.androidx.compose.foundation.layout)
            add("implementation", libs.androidx.compose.runtime.livedata)
            add("implementation", libs.androidx.compose.ui.util)
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }
}
