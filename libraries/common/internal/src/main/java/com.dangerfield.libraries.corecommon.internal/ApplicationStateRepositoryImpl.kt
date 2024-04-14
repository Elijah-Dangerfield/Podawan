package com.dangerfield.libraries.corecommon.internal

import android.content.res.Configuration
import com.dangerfield.libraries.coreflowroutines.AppScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import se.ansman.dagger.auto.AutoBind
import podawan.core.ApplicationState
import podawan.core.ApplicationStateRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Singleton
class ApplicationStateRepositoryImpl @Inject constructor(
    @AppScope private val appScope: CoroutineScope
): ApplicationStateRepository {

    private val appStateFlow = MutableStateFlow(ApplicationState.Foregrounded)
    private val configurationChanges = MutableSharedFlow<Configuration>()

    override fun applicationState(): StateFlow<ApplicationState> = appStateFlow

    override fun configurationChanges(): Flow<Configuration> = configurationChanges

    override fun onConfigurationChanged(newConfig: Configuration) {
        Timber.d("onConfigurationChanged: $newConfig")
        appScope.launch {
            configurationChanges.emit(newConfig)
        }
    }
    override fun onAppStart() {
        appScope.launch {
            appStateFlow.emit(ApplicationState.Foregrounded)
        }
    }

    override fun onAppStop() {
        appScope.launch {
            appStateFlow.emit(ApplicationState.Backgrounded)
        }
    }

    override fun onAppDestroyed() {
        appScope.launch {
            appStateFlow.emit(ApplicationState.Destroyed)
        }    }
}
