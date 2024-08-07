package com.dangerfield.features.consent.internal

import android.app.Activity
import com.dangerfield.features.consent.ConsentStatus
import com.dangerfield.features.consent.ForceEEAConsentLocation
import com.dangerfield.features.consent.toConsentStatus
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.collectIn
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import podawan.core.ApplicationState
import podawan.core.ApplicationStateRepository
import podawan.core.BuildInfo
import podawan.core.Catching
import podawan.core.doNothing
import podawan.core.logOnFailure
import podawan.core.showDebugSnack
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class GDRPConsentManager @Inject constructor(
    private val buildInfo: BuildInfo,
    private val forceEEAConsentLocation: ForceEEAConsentLocation,
    @AppScope private val appScope: CoroutineScope,
    applicationStateRepository: ApplicationStateRepository
) {

    init {
        applicationStateRepository.applicationState().collectIn(appScope) {
            when (it) {
                ApplicationState.Foregrounded -> doNothing()
                ApplicationState.Backgrounded -> doNothing()
                ApplicationState.Destroyed -> doNothing()
            }
        }
    }

    private val consentStatusFlow = MutableStateFlow<ConsentStatus?>(null)

    fun getConsentStatusFlow(activity: Activity): Flow<ConsentStatus> {
        refreshStatus(activity)
        return consentStatusFlow.filterNotNull()
    }

    private fun refreshStatus(activity: Activity) {
        appScope.launch {
            getConsentStatus(activity, dispatch = true)
        }
    }

    suspend fun updateConsentStatus(activity: Activity, status: ConsentStatus) {
        consentStatusFlow.value = status

        // get the updated info and check if it matches the status we were provided
        getConsentStatus(activity, dispatch = false).getOrNull()?.let { expectedStatus ->
            if (expectedStatus != status) {
                val message =
                    "Status given: $status does not match expected status: $expectedStatus"
                Timber.e(message)
                showDebugSnack { message }
            }
        }
    }

    fun resetConsentStatus(activity: Activity) {
        if (!buildInfo.isDebug && !buildInfo.isQA) return
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation.reset()
    }

    private suspend fun getConsentStatus(
        activity: Activity,
        dispatch: Boolean = true
    ): Catching<ConsentStatus> = Catching {
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)

        val debugSettings = ConsentDebugSettings.Builder(activity)
            .setForceTesting(buildInfo.isDebug)

        if (forceEEAConsentLocation()) {
            debugSettings.setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        }

        val params = ConsentRequestParameters
            .Builder()
            .setConsentDebugSettings(debugSettings.build())
            .build()

        getStatusAsync(
            consentInformation = consentInformation,
            activity = activity,
            params = params,
        )
    }.onSuccess { status ->
        if (dispatch) {
            consentStatusFlow.value = status
        }
    }
        .onFailure {
            consentStatusFlow.value = ConsentStatus.Unknown
        }

    private suspend fun getStatusAsync(
        consentInformation: ConsentInformation,
        activity: Activity,
        params: ConsentRequestParameters,
    ): ConsentStatus {
        return suspendCancellableCoroutine { continuation ->
            consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                {
                    if (consentInformation.canRequestAds()) {
                        doNothing() // no ads by default
                    }
                    Timber.d("Loading GDRP Consent status")
                    continuation.resume(consentInformation.toConsentStatus())
                },
                { requestConsentError: FormError ->
                    Timber.d("GDRP Consent Status Load failed: ${requestConsentError.message}")
                    continuation.resumeWithException(
                        ConsentFormError(requestConsentError.message)
                    )
                }
            )
        }
    }

    fun shouldShowSettingsOption(activity: Activity): Boolean = Catching {
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    }
        .logOnFailure()
        .getOrElse { false }


    class ConsentFormError(message: String) : Throwable(message)
}
