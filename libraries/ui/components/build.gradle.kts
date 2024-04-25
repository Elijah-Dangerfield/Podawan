
plugins {
    id("ooo.android.library")
}

podawan {
    compose()
    optIn("androidx.compose.material3.ExperimentalMaterial3Api")
}

android {
    namespace = "com.dangerfield.ui.components"
}
dependencies {
    implementation(projects.libraries.common)
    implementation(projects.libraries.ui)
    implementation(projects.libraries.ui.preview)

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

    implementation(libs.material)
}
