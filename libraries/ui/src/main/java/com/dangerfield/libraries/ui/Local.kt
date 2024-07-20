package com.dangerfield.libraries.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.color.Colors
import com.dangerfield.libraries.ui.typography.ConfiguredTypography
import podawan.core.AppVariantConfiguration
import podawan.core.AppState
import podawan.core.BuildInfo

val LocalColors = compositionLocalOf<Colors> {
    error("PodawanTheme wasn't applied")
}

val LocalContentColor = compositionLocalOf<ColorResource> {
    error("PodawanTheme wasn't applied")
}

val LocalTypography = compositionLocalOf<ConfiguredTypography> {
    error("PodawanTheme wasn't applied")
}

val LocalBuildInfo = staticCompositionLocalOf<BuildInfo> {
    error("No LocalBuildInfo provided")
}

val LocalAppState = staticCompositionLocalOf<AppState> {
    error("No LocalAppState provided")
}

val LocalAppVariantConfiguration = staticCompositionLocalOf<AppVariantConfiguration> {
    error("No AppConfiguration provided")
}