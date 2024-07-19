package com.dangerfield.libraries.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

val episodeIdArgument = navArgument("episodeIdArgument") {
    type = NavType.StringType
}
