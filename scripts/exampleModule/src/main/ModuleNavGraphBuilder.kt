package com.dangerfield.features.example.internal

import androidx.navigation.NavGraphBuilder
import se.ansman.dagger.auto.AutoBindIntoSet
import com.dangerfield.libraries.navigation.GlobalNavBuilder
import com.dangerfield.libraries.navigation.Router
import javax.inject.Inject

@AutoBindIntoSet
class ModuleNavGraphBuilder @Inject constructor(): GlobalNavBuilder {
    override fun NavGraphBuilder.addDestinations(router: Router) {
        TODO("Not yet implemented")
    }
}