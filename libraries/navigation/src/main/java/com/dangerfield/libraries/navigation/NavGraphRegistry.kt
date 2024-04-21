package com.dangerfield.libraries.navigation

import android.util.Log
import androidx.navigation.NavGraphBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavGraphRegistry @Inject constructor(
    private val homeTabNavBuilders: Set<@JvmSuppressWildcards HomeTabNavBuilder>,
    private val searchTabNavBuilders: Set<@JvmSuppressWildcards SearchTabNavBuilder>,
    private val libraryTabNavBuilders: Set<@JvmSuppressWildcards LibraryTabNavBuilder>,
    private val globalNavBuilders: Set<@JvmSuppressWildcards GlobalNavBuilder>,
    private val router: Router
) {

    fun addHomeDestinations(navGraphBuilder: NavGraphBuilder) {
        Log.d("Elijah", "Adding home destinations")
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
