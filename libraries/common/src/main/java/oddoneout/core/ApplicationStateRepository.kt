package podawan.core

import android.content.res.Configuration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ApplicationStateRepository {
    fun applicationState(): StateFlow<ApplicationState>
    fun configurationChanges(): Flow<Configuration>
    fun onAppStart()
    fun onConfigurationChanged(newConfig: Configuration)
    fun onAppStop()
    fun onAppDestroyed()
}

enum class ApplicationState {
    Foregrounded,
    Backgrounded,
    Destroyed
}
