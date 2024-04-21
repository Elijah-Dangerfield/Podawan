package com.dangerfield.libraries.navigation

import android.os.Parcelable
import androidx.annotation.IdRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NavPopUp(
    val popUpToRoute: String?,
    @IdRes val popUpToId: Int? = null,
    val popUpToInclusive: Boolean = false,
    val saveState: Boolean = false,
) : Parcelable

