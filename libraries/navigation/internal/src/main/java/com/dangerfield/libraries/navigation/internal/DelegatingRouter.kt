package com.dangerfield.libraries.navigation.internal

import androidx.compose.runtime.Stable
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.coreflowroutines.observeWithLifecycle
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.Router
import com.dangerfield.libraries.ui.components.dialog.bottomsheet.BottomSheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject
import javax.inject.Singleton

@AutoBind
@Stable
@Singleton
class DelegatingRouter @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
) : Router {

    private var delegate: Router? = null
    override val currentRouteName: String? get() = delegate?.currentRouteName
    private val rootNavigationRequests = Channel<Router.() -> Unit>(Channel.UNLIMITED)

    fun setRouter(router: Router, lifecycle: Lifecycle) {
        delegate = router

        appScope.launch {
            rootNavigationRequests.receiveAsFlow().observeWithLifecycle(lifecycle) { action ->
                action.invoke(router)
            }
        }
    }

    override fun navigate(filledRoute: Route.Filled) {
        // use continuations or something to wait until the delegate has been set
        rootNavigationRequests.trySend { navigate(filledRoute) }
    }

    override fun goBack() {
        rootNavigationRequests.trySend { goBack() }
    }

    override fun openWebLink(url: String, openInApp: Boolean) {
        rootNavigationRequests.trySend { openWebLink(url, openInApp) }
    }

    override fun popBackTo(route: Route.Template, inclusive: Boolean) {
        rootNavigationRequests.trySend { popBackTo(route, inclusive) }
    }

    override fun dismissSheet(sheetState: BottomSheetState) {
        rootNavigationRequests.trySend { dismissSheet(sheetState) }
    }

    override fun ifStillOn(backStackEntry: NavBackStackEntry, action: Router.() -> Unit) {
        rootNavigationRequests.trySend { ifStillOn(backStackEntry, action) }
    }

    override fun getBackStackEntry(route: Route.Template): NavBackStackEntry {
        return delegate?.getBackStackEntry(route) ?: throw IllegalStateException("Delegate not set")
    }
}