package com.dangerfield.features.qa.internal.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.components.ListItem
import com.dangerfield.libraries.ui.components.text.ProvideTextConfig
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.components.icon.Icon
import com.dangerfield.libraries.ui.components.icon.IconSize
import com.dangerfield.libraries.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.components.icon.PodawanIcon.ChevronRight
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun QANavigationItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    isDebug: Boolean = false,
    supportingText: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    ListItem(
        modifier = modifier.clickable(
            enabled = enabled,
            indication = null,
            interactionSource = interactionSource,
            onClick = onClick ?: {}
        ),
        trailingContent = {
            Icon(
                podawanIcon = ChevronRight("Navigate to option"),
                iconSize = IconSize.Large
            )
        },
        leadingContent = {
            if (isDebug) {
                Icon(podawanIcon = PodawanIcon.Bug(""))
            }
        },
        supportingContent = {
            ProvideTextConfig(typography = PodawanTheme.typography.Body.B600) {
                supportingText?.invoke()
            }
        },
        headlineContent = content
    )
}

@Preview
@Composable
private fun PreviewQANavigationItem() {
    Preview {
        QANavigationItem {
            Text("Title")
        }
    }
}


