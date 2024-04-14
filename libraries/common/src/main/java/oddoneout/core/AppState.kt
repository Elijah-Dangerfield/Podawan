package podawan.core

import kotlinx.coroutines.flow.StateFlow


interface AppState {
    val isOffline: StateFlow<Boolean>
}
