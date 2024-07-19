package com.podawan.plugin

import com.podawan.extension.AppExtension
import com.spyfall.podawan.util.BuildEnvironment
import com.podawan.util.SharedConstants
import com.spyfall.podawan.util.buildConfigField
import com.spyfall.podawan.util.checkForAppModuleSecretFiles
import com.spyfall.podawan.util.configureGitHooksCheck
import com.spyfall.podawan.util.configureKotlinAndroid
import com.spyfall.podawan.util.getVersionCode
import com.spyfall.podawan.util.getVersionName
import com.spyfall.podawan.util.libs
import com.spyfall.podawan.util.loadGradleProperty
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        target.configureGitHooksCheck()
        target.checkForAppModuleSecretFiles()

        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.create("podawan", AppExtension::class.java)

            extensions.configure<com.android.build.api.dsl.ApplicationExtension> {

                namespace = "com.dangerfield.podawan.${project.name}"

//                project.pluginManager.apply("kotlin-kapt")
//
//                project.extensions.configure(KaptExtension::class.java) {
//                    correctErrorTypes = true
//                }

                configureKotlinAndroid(this)

                defaultConfig.apply {
                    targetSdk = SharedConstants.targetSdk
                    versionName = getVersionName()
                    versionCode = getVersionCode()
                    applicationId = namespace
                    buildConfigField("VERSION_CODE", versionCode)
                    buildConfigField("VERSION_NAME", versionName)
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                        merges += "META-INF/LICENSE.md"
                        merges += "META-INF/LICENSE-notice.md"
                    }
                }

                buildTypes.getByName("debug").apply {
                    applicationIdSuffix = ".debug"
                }

                buildTypes.forEach {

                    it.buildConfigField("IS_QA", it.name.contains("qa", ignoreCase = true))

                    val isLocalReleaseBuild = !it.isDebuggable && !BuildEnvironment.isCIBuild
                    val releaseDebugSigningEnabled =
                        loadGradleProperty("podawan.releaseDebugSigningEnabled").toBoolean()

                    if (isLocalReleaseBuild && releaseDebugSigningEnabled) {
                        // set signing config to debug so that devs can test release builds locally without signing
                        it.signingConfig = signingConfigs.getByName("debug")
                        // prefix apk with indicator that the signing is invalid
                        archivesName.set("debugsigned-${archivesName.get()}")
                    }
                }

                dependencies {
                    add("implementation", libs.timber)
                }
            }
        }
    }
}
