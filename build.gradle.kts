
buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.google.services)
        classpath(libs.kotlinpoet)
        classpath(libs.dagger.hilt.gradle.plugin)
        classpath(libs.firebase.crashlytics.gradlePlugin)
        classpath(libs.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    id("ooo.android.detekt")
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlinAndroid) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.fabric.io/public")
    }
}

tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}


