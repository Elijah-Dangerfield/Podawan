package com.dangerfield.libraries.navigation.internal

import androidx.navigation.NavGraphBuilder
import com.dangerfield.libraries.navigation.HomeTabNavBuilder
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.LibraryTabNavBuilder
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.navigation.SearchTabNavBuilder
import javax.inject.Inject

class NavGraphRegistry @Inject constructor(
    private val homeTabNavBuilders: Set<@JvmSuppressWildcards HomeTabNavBuilder>,
    private val searchTabNavBuilders: Set<@JvmSuppressWildcards SearchTabNavBuilder>,
    private val libraryTabNavBuilders: Set<@JvmSuppressWildcards LibraryTabNavBuilder>,
    private val globalNavBuilders: Set<@JvmSuppressWildcards GlobalNavBuilder>,
    private val router: Router
) {

    fun addHomeDestinations(navGraphBuilder: NavGraphBuilder) {
        homeTabNavBuilders.forEach { moduleNavBuilder ->
            with(moduleNavBuilder) {
                navGraphBuilder.addDestinations(router)
            }
        }
    }
    
    fun addSearchDestinations(navGraphBuilder: NavGraphBuilder) {
        searchTabNavBuilders.forEach { moduleNavBuilder ->
            with(moduleNavBuilder) {
                navGraphBuilder.addDestinations(router)
            }
        }
    }
    
    fun addLibraryDestinations(navGraphBuilder: NavGraphBuilder) {
        libraryTabNavBuilders.forEach { moduleNavBuilder ->
            with(moduleNavBuilder) {
                navGraphBuilder.addDestinations(router)
            }
        }
    }
    
    fun addGlobalDestinations(navGraphBuilder: NavGraphBuilder) {
        globalNavBuilders.forEach { moduleNavBuilder ->
            with(moduleNavBuilder) {
                navGraphBuilder.addDestinations(router)
            }
        }
    }
}
