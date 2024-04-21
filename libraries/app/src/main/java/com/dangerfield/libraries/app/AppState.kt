package com.dangerfield.libraries.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.dangerfield.libraries.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import podawan.core.AppState

@Composable
fun rememberAppState(
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): PodawanAppState {
    return remember(
        coroutineScope,
        networkMonitor,
    ) {
        PodawanAppState(
            networkMonitor,
            coroutineScope,
        )
    }
}

/**
 * app state exposed through [com.dangerfield.libraries.ui.LocalAppState]
 * Should only contain fields that any individual screen might need to know without it being
 * ui state but rather the state of the app. feel free to add more properties as needed.
 */
@Stable
class PodawanAppState(
    networkMonitor: NetworkMonitor,
    val coroutineScope: CoroutineScope,
): AppState {

    override val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    override val isPlayingContent: StateFlow<Boolean>
        get() = MutableStateFlow(false)
}
