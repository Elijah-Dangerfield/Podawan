package com.dangerfield.libraries.session

import podawan.core.Catching

/**
 * Used to ensure the app session has requirments loaded.
 */
interface EnsureSessionLoaded {
    suspend operator fun invoke(): Catching<Unit>
}