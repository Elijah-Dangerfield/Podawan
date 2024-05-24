package com.dangerfield.features.library.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.VerticalSpacerD1200
import com.dangerfield.libraries.ui.VerticalSpacerD300
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.button.Button
import com.dangerfield.ui.components.text.Text

@Composable
fun EmptyLibraryScreen(
    onNewPlaylistClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Screen(modifier = modifier) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(horizontal = Dimension.D800),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Text(
               text = "Looks like you don't have any playlists yet.",
               textAlign = TextAlign.Center
           )

            VerticalSpacerD1200()

            Button(onClick = onNewPlaylistClicked) {
                Text(text = "New Playlist")
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.5f))
        }
    }
}


@Preview
@Composable
private fun PreviewLibraryScreen() {
    Preview {
        EmptyLibraryScreen()
    }
}