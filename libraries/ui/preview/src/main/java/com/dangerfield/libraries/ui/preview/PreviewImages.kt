package com.dangerfield.libraries.ui.preview

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import com.dangerfield.podawan.libraries.ui.preview.R

@Composable
fun previewableImage(
    @DrawableRes debugPreview: Int = R.drawable.placeholderlight,
    @DrawableRes res: Int? = null
) =
    if (LocalInspectionMode.current) {
    painterResource(id = debugPreview)
} else {
        res?.let { painterResource(id = it) }
}