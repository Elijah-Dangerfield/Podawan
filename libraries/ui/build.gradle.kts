
plugins {
    id("ooo.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

podawan {
    compose()
    flowroutines()
    optIn("androidx.compose.material3.ExperimentalMaterial3Api")
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.ext)
    implementation(libs.androidx.lifecycle.vm)
    implementation(libs.androidx.appcompat)
    implementation(libs.javax.inject)
    implementation(libs.androidx.core)
    implementation(libs.kotlin.std)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.customview.poolingcontainer)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(projects.libraries.common)

    api(projects.libraries.dictionary)
    api(libs.kmpalette.core)
}

android {
    namespace = "podawan.ui"
}
