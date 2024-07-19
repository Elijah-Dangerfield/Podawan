package com.podawan.extension


import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import com.podawan.util.SharedConstants
import com.google.devtools.ksp.gradle.KspExtension
import com.spyfall.podawan.util.configureAndroidCompose
import com.spyfall.podawan.util.getModule
import com.spyfall.podawan.util.libs
import com.spyfall.podawan.util.optInKotlinMarkers
import dev.zacsweers.moshix.ir.gradle.MoshiPluginExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import javax.inject.Inject

/*
TODO split into
App
Feature
AndroidLibrary
JavaLibrary
 */
@com.podawan.extension.PodawanExtensionDsl
abstract class AppExtension {
    @get:Inject
    internal abstract val project: Project

    fun optIn(vararg markerClasses: String) {
        project.optInKotlinMarkers(*markerClasses)
    }

    fun storage() {
        moshi()
        project.dependencies {
            add("implementation", getModule("libraries:storage"))
        }
    }

    fun room() {
        ksp()
        project.dependencies {
            "api"(project.libs.room)
            "api"(project.libs.room.runtime)
            "api"(project.libs.room.common)
            "ksp"(project.libs.room.compiler)
        }
    }

    fun daggerHilt(withProcessors: Boolean = true) {
        if (withProcessors) {
            project.pluginManager.apply("dagger.hilt.android.plugin")
            kapt {
                arguments {
                    arg("dagger.fastInit", "enabled")
                }
            }
        }

        project.dependencies {
            "implementation"(project.libs.dagger)
            "implementation"(project.libs.dagger.hilt.android)
            "implementation"(project.libs.dagger.hilt.core)
            "implementation"(project.libs.autoDagger.core)
            if (withProcessors) {
                val configuration = "kapt"
                configuration(project.libs.dagger.compiler)
                configuration(project.libs.dagger.hilt.compiler)
                configuration(project.libs.autoDagger.compiler)
            }
        }
    }

    fun compose() {
        val projectExt = project.extensions.findByType(LibraryExtension::class.java)
            ?: project.extensions.findByType(ApplicationExtension::class.java)
            ?: error(
                """
                Attempted to use compose outside of Application or Library project
                make sure you've applied one of the following plugin to the calling projects build.gradle:
                
                id("ooo.android.application")
                id("ooo.android.library")
                id("ooo.android.feature")

            """.trimIndent()
            )

        project.pluginManager.apply("dev.shreyaspatil.compose-compiler-report-generator")

        project.configureAndroidCompose(projectExt, false)
    }

    fun flowroutines() {
        project.dependencies {
            add("implementation", getModule("libraries:flowroutines"))
        }
    }

    fun firebase() {
        project.dependencies {
            "implementation"(platform(project.libs.firebase.bom))
            add("implementation", project.libs.firebase.database)
            add("implementation", project.libs.firebase.auth)
            add("implementation", project.libs.firebase.firestore)
            add("implementation", project.libs.firebase.analytics)
            add("implementation", project.libs.firebase.crashlytics)
            add("implementation", project.libs.firebase.storage)
            add("implementation", project.libs.firebase.database)
            add("implementation", project.libs.kotlinx.coroutines.play.services)
        }
    }

    fun kapt(configure: KaptExtension.() -> Unit = {}) {
        project.pluginManager.apply("kotlin-kapt")
        project.extensions.configure(configure)
    }

    open fun ksp(configure: KspExtension.() -> Unit = {}) {
        project.pluginManager.apply("com.google.devtools.ksp")
        project.extensions.configure(configure)
    }

    open fun moshi(configure: MoshiPluginExtension.() -> Unit = {}) {
        project.plugins.apply("dev.zacsweers.moshix")
        // Needed for MoshiX
        ksp()
        project.dependencies {
            "implementation"(project.libs.moshi)
        }
        project.extensions.configure(configure)
    }

    fun serialization() {
        project.dependencies {
            add("implementation",project.libs.kotlinx.serialization.json)
        }
    }

    fun unitTesting() {
        with(project.pluginManager) {
            apply("com.android.test")
            apply("org.jetbrains.kotlin.android")
        }

        project.extensions.configure<TestExtension> {
            defaultConfig.targetSdk = SharedConstants.testingTargetSdk
        }
    }

    fun networking() {
        TODO("Not yet implemented")
    }
}

private val Project.android: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType(CommonExtension::class.java)
