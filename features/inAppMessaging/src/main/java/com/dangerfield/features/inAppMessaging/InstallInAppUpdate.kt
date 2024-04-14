package com.dangerfield.features.inAppMessaging

import podawan.core.Catching

/**
 * Installs the downloaded in app update
 */
interface InstallInAppUpdate {
    suspend operator fun invoke(): Catching<Unit>
}