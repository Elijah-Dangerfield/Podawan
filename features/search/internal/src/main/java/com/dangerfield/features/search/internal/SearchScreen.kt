package com.dangerfield.features.search.internal

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.VerticalSpacerD1000
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.libraries.ui.pulsate
import com.dangerfield.ui.components.Screen
import com.dangerfield.ui.components.icon.Icon
import com.dangerfield.ui.components.icon.IconSize
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    Screen(
        modifier = modifier,
        topBar = {
            Text(
                modifier = Modifier.padding(Dimension.D800),
                text = "Search",
                textAlign = TextAlign.Center
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                podawanIcon = PodawanIcon.Search(null),
                iconSize = IconSize.Large
            )

            VerticalSpacerD1000()

            Text(
                modifier = Modifier.pulsate(),
                text = "This feature has not been implemented yet",
                textAlign = TextAlign.Center
            )

        }
    }
}

@Preview
@Composable
private fun PreviewSearchScreen() {
    Preview {
        SearchScreen()
    }
}