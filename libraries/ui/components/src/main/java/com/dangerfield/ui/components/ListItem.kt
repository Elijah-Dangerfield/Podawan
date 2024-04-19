package com.dangerfield.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dangerfield.libraries.ui.preview.Preview
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.ui.components.text.ProvideTextConfig
import com.dangerfield.ui.components.text.Text
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    onClickItem: () -> Unit = {},
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit) = {},
    trailingContent: @Composable (() -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickItem() }
            .padding(vertical = Dimension.D500),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingContent.let {
            it()
            Spacer(modifier = Modifier.width(Dimension.D500))
        }

        Column(modifier = Modifier.weight(1f)) {
            ProvideTextConfig(
                typography = PodawanTheme.typography.Heading.H700
            ) {
                headlineContent()
            }
            ProvideTextConfig(
                typography = PodawanTheme.typography.Body.B600
            ) {
                supportingContent?.let { it() }
            }
        }
        Spacer(modifier = Modifier.width(Dimension.D500))
        trailingContent?.let {
            Box {
                it()
            }
        }
    }
}

@Composable
@Preview
fun PreviewSettingsOption() {
    com.dangerfield.libraries.ui.preview.Preview(showBackground = true) {
        ListItem(
            headlineContent =
            {
                Text(text = "Headline")
            },
            supportingContent = {
                Text(text = "Supporting")
            },

            leadingContent = {
                Icon(PodawanIcon.Android("Android"))
            },

            trailingContent = {
                Icon(PodawanIcon.Android("Android"))
            }
        )
    }
}

@Composable
@Preview
fun PreviewSettingsOptionNoLeading() {
    com.dangerfield.libraries.ui.preview.Preview(showBackground = true) {
        ListItem(
            headlineContent =
            {
                Text(text = "Headline")
            },
            supportingContent = {
                Text(text = "Supporting")
            },

            trailingContent = {
                Icon(PodawanIcon.Android("Android"))
            }
        )
    }
}

