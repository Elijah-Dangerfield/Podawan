package com.dangerfield.features.search.internal

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dangerfield.features.search.navigateToSearchA
import com.dangerfield.features.search.navigateToSearchB
import com.dangerfield.features.search.navigateToSearchC
import com.dangerfield.features.search.searchA
import com.dangerfield.features.search.searchB
import com.dangerfield.features.search.searchC
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.SearchTabNavBuilder
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.button.Button
import com.dangerfield.libraries.ui.components.text.Text
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): SearchTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = searchRoute.navRoute,
            arguments = searchRoute.navArguments
        ) {
            Screen {
                Text(text = "Search")

                Button(onClick = {
                    router.navigateToSearchA()
                }) {
                    Text(text = "To Search A")
                }
            }
        }

        composable(
            route = searchA.navRoute,
            arguments = searchA.navArguments
        ) {
            Screen {
                Text("Search A")
                Button(onClick = {
                    router.navigateToSearchB()
                }) {
                    Text("To Search B")
                }
            }
        }

        composable(
            route = searchB.navRoute,
            arguments = searchB.navArguments
        ) {
            Screen {
                Text("Search B")
                Button(onClick = {
                    router.navigateToSearchC()
                }) {
                    Text("To Search C")
                }
            }
        }

        composable(
            route = searchC.navRoute,
            arguments = searchC.navArguments
        ) {
            Screen {
                Text("Search C")
            }
        }
    }
}