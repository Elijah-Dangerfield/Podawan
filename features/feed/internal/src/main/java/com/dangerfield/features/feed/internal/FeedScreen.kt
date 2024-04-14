package com.dangerfield.features.feed.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.components.Screen
import com.dangerfield.libraries.ui.components.button.Button
import com.dangerfield.libraries.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun FeedScreen(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Screen(modifier) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = Dimension.D1000)) {

            VerticalSpacerD1000()

            Text(
                text = "Feed",
                typography = PodawanTheme.typography.Heading.H1000
            )

            VerticalSpacerD1000()

            Button(
                onClick = { onClick() }) {
                Text(text = "To Feed A")
            }
        }
    }
}

@Composable
@Preview
private fun PreviewScreen() {
    Preview {
        FeedScreen(
        )
    }
}