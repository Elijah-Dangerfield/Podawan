package com.podawan.plugin

import com.android.build.gradle.LibraryExtension
import com.podawan.util.SharedConstants
import com.spyfall.podawan.util.configureKotlinAndroid
import com.spyfall.podawan.util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * This plugin can be applied in a library module in the build.gradle.kts file
 * using the following syntax
 * ```kotlin
 * plugins {
 *     id("ooo.android.library")
 * }
 * ```
 * library modules should not depend on android specific frameworks.
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            if (extensions.findByName("podawan") == null) {
                extensions.create("podawan", com.podawan.extension.LibraryExtension::class.java)
            }

            /*
            TODO
            not all libraries should automatically get the android stuff, lets consider
            making things different
             */
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = SharedConstants.targetSdk
            }

            dependencies {
                add("implementation", libs.timber)
            }
        }
    }
}
