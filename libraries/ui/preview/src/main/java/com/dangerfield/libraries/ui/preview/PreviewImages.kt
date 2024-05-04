package com.dangerfield.libraries.ui.preview

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import com.dangerfield.podawan.libraries.ui.preview.R

@Composable
fun previewableImage(
    @DrawableRes debugPreview: Int = R.drawable.placeholderlight,
    @DrawableRes actualImage: Int? = null
) =
    if (LocalInspectionMode.current) {
    painterResource(id = debugPreview)
} else {
    actualImage?.let { painterResource(id = it) }
}