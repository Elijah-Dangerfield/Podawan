package com.dangerfield.features.playlist.internal

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.HorizontalSpacerD500
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.ui.components.icon.CircleIcon
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text

@Composable
fun AddToPlaylistItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimension.D500)
            .bounceClick(onClick = onClick)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CircleIcon(
            icon = PodawanIcon.Add(""),
            iconSize = IconSize.Medium,
        )

        HorizontalSpacerD500()

        Text(
            maxLines = 2,
            text = "Add to playlist",
            typography = PodawanTheme.typography.Body.B600.Bold
        )
    }

}

@Composable
@Preview
private fun PreviewAddToPlaylistItem() {
    Preview {
        AddToPlaylistItem(
        )
    }
}