package com.dangerfield.features.qa.internal.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.ui.components.ListItem
import com.dangerfield.ui.components.Switch
import com.dangerfield.ui.components.text.ProvideTextConfig
import com.dangerfield.ui.components.text.Text
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun QASwitchItem(
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    isDebug: Boolean = false,
    supportingText: (@Composable () -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    ListItem(
        modifier = modifier.clickable(
            role = Role.Checkbox,
            enabled = enabled,
            indication = null,
            interactionSource = interactionSource,
            onClick = onClick ?: { onCheckedChanged(!checked) }
        ),
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChanged,
                enabled = enabled,
                interactionSource = interactionSource
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
        headlineContent = headlineContent
    )
}

@Preview
@Composable
private fun QAItem() {
    com.dangerfield.libraries.ui.preview.Preview {
        var checked by rememberSaveable { mutableStateOf(false) }
        QASwitchItem(
            checked = checked,
            onCheckedChanged = { checked = it },
            supportingText = { Text("Supporting text") }
        ) {
            Text("Check me")
        }
    }
}
