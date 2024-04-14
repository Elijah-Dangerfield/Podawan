package com.podawan.plugin

import com.spyfall.podawan.util.configureKotlinJvm
import com.podawan.extension.JavaExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class JavaLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {

            if (extensions.findByName("podawan") == null) {
                extensions.create("podawan", JavaExtension::class.java)
            }

            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }

            configureKotlinJvm()
        }
    }
}
