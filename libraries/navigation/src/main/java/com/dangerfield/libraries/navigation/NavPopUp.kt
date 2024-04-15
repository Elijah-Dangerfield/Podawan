package com.dangerfield.libraries.navigation

import androidx.annotation.IdRes

data class NavPopUp(
    val popUpToRoute: String?,
    @IdRes val popUpToId: Int? = null,
    val popUpToInclusive: Boolean = false,
    val saveState: Boolean = false,
)

