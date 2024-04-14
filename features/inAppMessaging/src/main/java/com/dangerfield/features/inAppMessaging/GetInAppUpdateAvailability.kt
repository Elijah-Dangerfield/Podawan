package com.dangerfield.features.inAppMessaging

import com.google.android.play.core.appupdate.AppUpdateInfo
import podawan.core.Catching

/**
 * Returns the availability of an in app update
 */
interface GetInAppUpdateAvailability {
    suspend operator fun invoke(): Catching<InAppUpdateAvailability>
}

sealed class InAppUpdateAvailability {

    /**
     * Represents the availability of an in app update
     * @param appUpdateInfo The [AppUpdateInfo] of the available update
     * @param shouldShow Whether the update should be shown to the user (we dont show every available update)
     * @param isForegroundUpdate Whether the update is a foreground update
     */
    class UpdateAvailable(
        val appUpdateInfo: AppUpdateInfo,
        val shouldShow: Boolean,
        val isForegroundUpdate: Boolean
    ) : InAppUpdateAvailability()

    /**
     * Represents that an update is currently in progress
     */
    data object UpdateInProgress : InAppUpdateAvailability()

    /**
     * Represents that no update is available
     */
    data object NoUpdateAvailable : InAppUpdateAvailability()

    /**
     * Represents that an update is ready to install
     */
    data class UpdateReadyToInstall(val wasDownloadedInBackground: Boolean): InAppUpdateAvailability()
}