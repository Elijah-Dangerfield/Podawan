package com.dangerfield.libraries.navigation.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dangerfield.features.feed.feedRoute
import com.dangerfield.features.library.libraryRoute
import com.dangerfield.features.search.searchRoute
import com.dangerfield.libraries.ui.Border
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.components.Badge
import com.dangerfield.libraries.ui.components.BadgedBox
import com.dangerfield.libraries.ui.components.Surface
import com.dangerfield.libraries.ui.components.icon.Icon
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun BottomBar(
    items: List<BottomBarItem>,
    onItemClick: (BottomBarItem) -> Unit) {

    NavigationBar(
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = Modifier.bounceClick(),
                colors = ItemColors,
                selected = item.isSelected,
                onClick = {
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
                            podawanIcon = if (item.isSelected) item.selectedIcon else item.unselectedIcon,
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

@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit
) {

    Surface(
        modifier = modifier,
        elevation = Elevation.Button,
        radius = Radii.BottomBar,
        color = PodawanTheme.colors.surfaceSecondary,
        contentColor = PodawanTheme.colors.onSurfaceSecondary,
        border = null,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
                .defaultMinSize(minHeight = 80.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
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
    val route: String,
    val isSelected: Boolean,
    val selectedIcon: PodawanIcon,
    val unselectedIcon: PodawanIcon,
    val badgeAmount: Int = 0
) {
    companion object {
        fun Home(isSelected: Boolean) = BottomBarItem(
            title = "Home",
            route = feedRoute.navRoute,
            isSelected = isSelected,
            selectedIcon = PodawanIcon.HomeFilled("Home"),
            unselectedIcon = PodawanIcon.HomeOutline("Home")
        )
        fun Search(isSelected: Boolean) = BottomBarItem(
            title = "Search",
            route = searchRoute.navRoute,
            selectedIcon = PodawanIcon.SearchFilled("Search"),
            unselectedIcon = PodawanIcon.SearchOutline("Search"),
            badgeAmount = 2,
            isSelected = isSelected
        )
        fun Library(isSelected: Boolean) = BottomBarItem(
            title = "Library",
            route = libraryRoute.navRoute,
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
        BottomBar(
            items = listOf(
                BottomBarItem.Home(true),
                BottomBarItem.Search(false),
                BottomBarItem.Library(false)
            ),
            onItemClick = {}
        )
    }
}