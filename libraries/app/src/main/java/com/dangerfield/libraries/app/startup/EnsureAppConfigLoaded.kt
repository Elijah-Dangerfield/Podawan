package com.dangerfield.libraries.app.startup

import com.dangerfield.libraries.config.AppConfigRepository
import kotlinx.coroutines.flow.first
import podawan.core.Catching
import podawan.core.ignoreValue
import podawan.core.throwIfDebug
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ensure that the app config is loaded before the app starts
 */
@Singleton
class EnsureAppConfigLoaded @Inject constructor(
    private val appConfigRepository: AppConfigRepository
) {
    suspend operator fun invoke(): Catching<Unit> =
        Catching {
            appConfigRepository.configStream().first()
        }
            .throwIfDebug()
            .ignoreValue()
}