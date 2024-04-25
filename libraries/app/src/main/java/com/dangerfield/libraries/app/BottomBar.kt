package com.dangerfield.libraries.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dangerfield.libraries.navigation.BottomBarMaxHeight
import com.dangerfield.libraries.navigation.BottomBarMinHeight
import com.dangerfield.libraries.navigation.NavAnimType
import com.dangerfield.libraries.navigation.Route
import com.dangerfield.libraries.navigation.fill
import com.dangerfield.libraries.navigation.route
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.libraries.ui.LocalAppState
import com.dangerfield.libraries.ui.elevation
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.thenIf
import com.dangerfield.ui.components.Badge
import com.dangerfield.ui.components.BadgedBox
import com.dangerfield.ui.components.HorizontalDivider
import com.dangerfield.ui.components.Surface
import podawan.core.App

val homeGraphRoute = route("homeGraph") {
    isTopLevel(false)
    navAnimType(NavAnimType.None)
}

val searchGraphRoute = route("searchGraph") {
    isTopLevel(false)
    navAnimType(NavAnimType.None)
}

val libraryGraphRoute = route("libraryGraph") {
    isTopLevel(false)
    navAnimType(NavAnimType.None)
}

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    currentTabRoute: Route.Template,
    onItemClick: (Route.Template) -> Unit
) {
    val isPlayingContent by LocalAppState.current.isPlayingContent.collectAsStateWithLifecycle()

    val tabs = listOf(
        BottomBarItem.Home(currentTabRoute == homeGraphRoute),
        BottomBarItem.Search(currentTabRoute == searchGraphRoute),
        BottomBarItem.Library(currentTabRoute == libraryGraphRoute)
    )

    Column {

        val isDark = PodawanTheme.colors.background.color.luminance() < 0.5f

        if (!isPlayingContent && isDark) {
            HorizontalDivider()
        }

        NavigationBar(
            modifier.thenIf(!isPlayingContent &&!isDark) { elevation(Elevation.BottomBar) }
        ) {
            tabs.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = itemColors(),
                    selected = item.isSelected,
                    onClick = { onItemClick(item.route) },
                    icon = {
                        BadgedBox(badge = {
                            if (item.badgeAmount > 0) {
                                BottomBarBadge(item.badgeAmount)
                            }
                        }
                        ) {
                            Icon(
                                podawanIcon = if (item.isSelected) item.selectedIcon else item.unselectedIcon,
                            )
                        }
                    },
                    label = {
                        Text(
                            item.title,
                            typography = if (item.isSelected) {
                                PodawanTheme.typography.Label.L500.SemiBold
                            } else {
                                PodawanTheme.typography.Label.L500.Normal
                            }
                        )
                    })
            }
        }
    }
}

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .heightIn(min = BottomBarMinHeight, max = BottomBarMaxHeight),
        elevation = Elevation.Button,
        color = PodawanTheme.colors.background,
        contentColor = PodawanTheme.colors.onBackground,
        border = null,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .defaultMinSize(minHeight = BottomBarMinHeight)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun itemColors(): NavigationBarItemColors {
    return NavigationBarItemColors(
        selectedIconColor = PodawanTheme.colors.background.color,
        unselectedIconColor = PodawanTheme.colors.onBackground.color,

        selectedTextColor = PodawanTheme.colors.onBackground.color,
        selectedIndicatorColor = PodawanTheme.colors.onBackground.color,

        unselectedTextColor = PodawanTheme.colors.onSurfaceDisabled.color,
        disabledIconColor = PodawanTheme.colors.onSurfaceDisabled.color,
        disabledTextColor = PodawanTheme.colors.onSurfaceDisabled.color,
    )

}

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
    val isSelected: Boolean,
    val selectedIcon: PodawanIcon,
    val unselectedIcon: PodawanIcon,
    val badgeAmount: Int = 0
) {
    companion object {
        fun Home(isSelected: Boolean) = BottomBarItem(
            title = "Home",
            route = homeGraphRoute,
            isSelected = isSelected,
            selectedIcon = PodawanIcon.HomeFilled("Home"),
            unselectedIcon = PodawanIcon.HomeOutline("Home")
        )

        fun Search(isSelected: Boolean) = BottomBarItem(
            title = "Search",
            route = searchGraphRoute,
            selectedIcon = PodawanIcon.SearchFilled("Search"),
            unselectedIcon = PodawanIcon.SearchOutline("Search"),
            isSelected = isSelected
        )

        fun Library(isSelected: Boolean) = BottomBarItem(
            title = "Library",
            route = libraryGraphRoute,
            selectedIcon = PodawanIcon.LibraryFilled("Library"),
            unselectedIcon = PodawanIcon.LibraryOutline("Library"),
            isSelected = isSelected
        )
    }
}


@Preview
@Composable
private fun BottomBarPreview() {
    Preview {
        AppBottomBar(
            currentTabRoute = homeGraphRoute,
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun BottomBarPreviewStuffYouShouldKnow() {
    Preview(
        app = App.StuffYouShouldKnow
    ) {
        AppBottomBar(
            currentTabRoute = homeGraphRoute,
            onItemClick = {}
        )
    }
}