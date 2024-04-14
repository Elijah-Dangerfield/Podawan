package com.dangerfield.features.inAppMessaging.internal.update

import com.dangerfield.features.inAppMessaging.InstallInAppUpdate
import com.google.android.play.core.appupdate.AppUpdateManager
import podawan.core.Catching
import podawan.core.awaitCatching
import podawan.core.ignoreValue
import podawan.core.logOnFailure
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

@AutoBind
class InstallInAppUpdateImpl @Inject constructor(
    private val appUpdateManager: AppUpdateManager
) : InstallInAppUpdate {
    override suspend fun invoke(): Catching<Unit> {
        return appUpdateManager
            .completeUpdate()
            .awaitCatching()
            .logOnFailure()
            .ignoreValue()
    }
}