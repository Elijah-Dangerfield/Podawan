package com.dangerfield.libraries.network.internal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dangerfield.libraries.dictionary.dictionaryString
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.oddoneoout.libraries.network.internal.R
import com.dangerfield.ui.components.text.Text

@Composable
fun OfflineBar(isOffline: Boolean) {
    AnimatedVisibility(
        visible = isOffline,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PodawanTheme.colors.textWarning.color),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dictionaryString(R.string.odd_one_out_is_offline),
                typography = PodawanTheme.typography.Body.B700,
                colorResource = PodawanTheme.colors.text,
            )
        }
    }
}
