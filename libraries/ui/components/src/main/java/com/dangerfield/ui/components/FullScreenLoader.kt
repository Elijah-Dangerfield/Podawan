package com.dangerfield.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PodawanTheme.colors.background.color),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}