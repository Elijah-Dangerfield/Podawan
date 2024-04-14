package com.dangerfield.libraries.navigation.internal

import androidx.navigation.NavGraphBuilder
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.TopLevelNavBuilder
import javax.inject.Inject

class NavGraphRegistry @Inject constructor(
    private val homeTabNavBuilders: Set<@JvmSuppressWildcards HomeTabNavBuilder>,
//    private val searchTabNavBuilders: Set<@JvmSuppressWildcards SearchTabNavBuilder>,
//    private val libraryTabNavBuilders: Set<@JvmSuppressWildcards LibraryTabNavBuilder>,
    //private val topLevelNavBuilders: Set<@JvmSuppressWildcards TopLevelNavBuilder>,
    private val router: Router
) {

    fun addRegistrations(navGraphBuilder: NavGraphBuilder) {
        homeTabNavBuilders.forEach { moduleNavBuilder ->
            with(moduleNavBuilder) {
                navGraphBuilder.addDestinations(router)
            }
        }
    }
}
