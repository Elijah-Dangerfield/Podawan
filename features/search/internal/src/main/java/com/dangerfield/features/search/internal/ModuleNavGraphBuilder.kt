package com.dangerfield.features.search.internal

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import se.ansman.dagger.auto.AutoBindIntoSet
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): SearchTabNavBuilder {

    override fun NavGraphBuilder.addDestinations(router: Router) {
        composable(
            route = searchRoute.navRoute,
            arguments = searchRoute.navArguments
        ) {
            Screen(
                topBar = {
                    Text(
                        modifier = Modifier.padding(Dimension.D800),
                        text = "Search",
                        textAlign = TextAlign.Center
                    )
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        podawanIcon = PodawanIcon.Search(null),
                        iconSize = IconSize.Large
                    )

                    VerticalSpacerD1000()

                    Text(text = "This feature has not been implemented yet, ")

                }
            }
        }
    }
}