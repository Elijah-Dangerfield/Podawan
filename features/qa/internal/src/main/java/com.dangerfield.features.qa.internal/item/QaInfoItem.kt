package com.dangerfield.features.qa.internal.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.ui.components.ListItem
import com.dangerfield.ui.components.text.ProvideTextConfig
import com.dangerfield.ui.components.text.Text
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun QaInfoItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    isDebug: Boolean = false,
    supportingText: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
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
            ProvideTextConfig(typography = PodawanTheme.typography.Body.B500) {
                trailingContent?.invoke()
            }
        },
        supportingContent = {
            ProvideTextConfig(typography = PodawanTheme.typography.Body.B600) {
                supportingText?.invoke()
            }
        },
        leadingContent = {
              if (isDebug) {
                  Icon(podawanIcon = PodawanIcon.Bug(""))
              }
        },
        headlineContent = headlineContent
    )
}

@Preview
@Composable
private fun PreviewQaInfoItemItem() {
    com.dangerfield.libraries.ui.preview.Preview {
        QaInfoItem {
            Text("Title")
        }
    }
}


