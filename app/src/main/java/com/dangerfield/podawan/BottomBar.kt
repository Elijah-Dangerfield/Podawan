package com.dangerfield.podawan

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.route
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.components.Badge
import com.dangerfield.libraries.ui.components.BadgedBox
import com.dangerfield.libraries.ui.components.icon.Icon
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun BottomBar(items: List<BottomBarItem>, onItemClick: (BottomBarItem) -> Unit) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = PodawanTheme.colors.surfaceSecondary.color,
        contentColor = PodawanTheme.colors.onSurfaceSecondary.color
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = ItemColors,
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onItemClick(item)
                },
                icon = {
                    BadgedBox(badge = {
                        if (item.badgeAmount > 0) {
                            BottomBarBadge(item.badgeAmount)
                        }
                    }
                    ) {
                        Icon(
                            podawanIcon = if (selectedTabIndex == index) item.selectedIcon else item.unselectedIcon,
                        )
                    }
                },
                label = {
                    Text(
                        item.title, typography = PodawanTheme.typography.Label.L500.SemiBold,
                    )
                })
        }
    }
}

val ItemColors: NavigationBarItemColors
    @Composable get() = NavigationBarItemColors(
        selectedIconColor = PodawanTheme.colors.surfaceSecondary.color,
        selectedTextColor = PodawanTheme.colors.onSurfaceSecondary.color,
        selectedIndicatorColor = PodawanTheme.colors.onSurfaceSecondary.color,
        unselectedIconColor = PodawanTheme.colors.onSurfaceSecondary.color,
        unselectedTextColor = PodawanTheme.colors.onSurfaceDisabled.color,
        disabledIconColor = PodawanTheme.colors.onSurfaceDisabled.color,
        disabledTextColor = PodawanTheme.colors.onSurfaceDisabled.color,
    )


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarBadge(count: Int? = null) {
    if (count != null) {
        Badge {
            Text(
                count.toString(),
                typography = PodawanTheme.typography.Body.B400,
            )
        }
    }
}

data class BottomBarItem(
    val title: String,
    val route: Route.Template,
    val selectedIcon: PodawanIcon,
    val unselectedIcon: PodawanIcon,
    val badgeAmount: Int = 0
) {
    companion object {
        val Home = BottomBarItem(
            title = "Home",
            route = feedRoute,
            selectedIcon = PodawanIcon.HomeFilled("Home"),
            unselectedIcon = PodawanIcon.HomeOutline("Home")
        )
        val Search = BottomBarItem(
            title = "Search",
            route = route("search"),
            selectedIcon = PodawanIcon.SearchFilled("Search"),
            unselectedIcon = PodawanIcon.SearchOutline("Search"),
            badgeAmount = 2
        )
        val Library = BottomBarItem(
            title = "Library",
            route = route("library"),
            selectedIcon = PodawanIcon.LibraryFilled("Library"),
            unselectedIcon = PodawanIcon.LibraryOutline("Library"),
        )
    }
}


@Preview
@Composable
private fun BottomBarPreview() {
    Preview {
        BottomBar(
            items = listOf(
                BottomBarItem(
                    title = "Home",
                    route = route("home"),
                    selectedIcon = PodawanIcon.HomeFilled("Home"),
                    unselectedIcon = PodawanIcon.HomeOutline("Home")
                ),
                BottomBarItem(
                    title = "Search",
                    route = route("search"),
                    badgeAmount = 2,
                    selectedIcon = PodawanIcon.SearchFilled("Search"),
                    unselectedIcon = PodawanIcon.SearchOutline("Search")
                ),
                BottomBarItem(
                    title = "Library",
                    route = route("library"),
                    selectedIcon = PodawanIcon.LibraryFilled("Library"),
                    unselectedIcon = PodawanIcon.LibraryOutline("Library"),
                )
            ),
            onItemClick = {}
        )
    }
}