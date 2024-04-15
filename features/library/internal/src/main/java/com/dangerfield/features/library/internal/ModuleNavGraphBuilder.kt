package com.dangerfield.features.library.internal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.libraries.navigation.LibraryTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.text.Text
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): LibraryTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = libraryRoute.navRoute,
            arguments = libraryRoute.navArguments
        ) {
           Screen {
               Text(text = "Library")
           }
        }
    }
}