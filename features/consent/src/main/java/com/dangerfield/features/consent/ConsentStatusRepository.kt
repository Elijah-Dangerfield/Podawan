package com.dangerfield.features.consent

import android.app.Activity
import android.os.Parcelable
import com.google.android.ump.ConsentInformation
import kotlinx.coroutines.flow.Flow
import kotlinx.parcelize.Parcelize

interface ConsentStatusRepository {
    fun getStatusFlow(activity: Activity,): Flow<ConsentStatus>
}

@Parcelize
enum class ConsentStatus : Parcelable {
    ConsentGiven,
    ConsentDenied,
    ConsentNeeded,
    ConsentNotNeeded,
    Unknown
}

fun ConsentInformation.toConsentStatus() = when (consentStatus) {
    ConsentInformation.ConsentStatus.REQUIRED -> ConsentStatus.ConsentNeeded
    ConsentInformation.ConsentStatus.NOT_REQUIRED -> ConsentStatus.ConsentNotNeeded
    ConsentInformation.ConsentStatus.UNKNOWN -> ConsentStatus.Unknown
    ConsentInformation.ConsentStatus.OBTAINED -> {
        if (canRequestAds()) {
            ConsentStatus.ConsentGiven
        } else {
            ConsentStatus.ConsentDenied
        }
    }

    else -> ConsentStatus.Unknown
}