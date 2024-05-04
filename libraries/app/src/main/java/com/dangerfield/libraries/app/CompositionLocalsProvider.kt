package com.dangerfield.libraries.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import com.dangerfield.libraries.analytics.LocalMetricsTracker
import com.dangerfield.libraries.analytics.MetricsTracker
import com.dangerfield.libraries.dictionary.Dictionary
import com.dangerfield.libraries.dictionary.LocalDictionary
import com.dangerfield.libraries.network.NetworkMonitor
import com.dangerfield.libraries.ui.LocalAppState
import com.dangerfield.libraries.ui.LocalBuildInfo
import com.dangerfield.libraries.ui.LocalAppConfiguration
import com.dangerfield.libraries.ui.LocalColors
import com.dangerfield.libraries.ui.color.Colors
import podawan.core.AppConfiguration
import podawan.core.BuildInfo
import javax.inject.Inject

/**
 * Provides all the composition locals for the app
 * If a module needs to provide a local, it should define the local in its public module, implement it
 * in its internal module and then inject the implementation to be provided here.
 *
 * NOTE: Locals should be rare. First consider injecting the class into the Features Graph Builder
 * and passing into the composable as a parameter.
 */
@Stable
class CompositionLocalsProvider @Inject constructor(
    private val metricsTracker: MetricsTracker,
    private val buildInfo: BuildInfo,
    private val dictionary: Dictionary,
    private val colors: Colors,
    private val networkMonitor: NetworkMonitor,
    private val appConfiguration: AppConfiguration
){

    @Composable
    operator fun invoke(content: @Composable () -> Unit) {

        val appState = rememberAppState(networkMonitor = networkMonitor)

        CompositionLocalProvider(
            LocalColors provides colors,
            LocalAppConfiguration provides appConfiguration,
            LocalMetricsTracker provides metricsTracker,
            LocalDictionary provides dictionary,
            LocalBuildInfo provides buildInfo,
            LocalAppState provides appState,
            content = content
        )
    }
}