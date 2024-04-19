package com.dangerfield.ui.components.icon

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.ui.Dimension.D1000
import com.dangerfield.libraries.ui.Dimension.D1200
import com.dangerfield.libraries.ui.Dimension.D1300
import com.dangerfield.libraries.ui.Dimension.D600
import com.dangerfield.libraries.ui.Dimension.D800
import com.dangerfield.libraries.ui.preview.Preview
import com.dangerfield.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun Icon(
    podawanIcon: PodawanIcon,
    modifier: Modifier = Modifier,
    iconSize: IconSize = IconSize.Small,
    tint: Color = LocalContentColor.current
) {
    androidx.compose.material3.Icon(
        imageVector = podawanIcon.imageVector,
        contentDescription = podawanIcon.contentDescription,
        modifier = modifier.size(iconSize.dp),
        tint = tint
    )
}

@Composable
fun SmallIcon(
    podawanIcon: PodawanIcon,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    androidx.compose.material3.Icon(
        imageVector = podawanIcon.imageVector,
        contentDescription = podawanIcon.contentDescription,
        modifier = modifier.size(IconSize.Small.dp),
        tint = tint
    )
}

@Composable
fun MediumIcon(
    podawanIcon: PodawanIcon,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    androidx.compose.material3.Icon(
        imageVector = podawanIcon.imageVector,
        contentDescription = podawanIcon.contentDescription,
        modifier = modifier.size(IconSize.Medium.dp),
        tint = tint
    )
}

@Composable
fun LargeIcon(
    podawanIcon: PodawanIcon,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    androidx.compose.material3.Icon(
        imageVector = podawanIcon.imageVector,
        contentDescription = podawanIcon.contentDescription,
        modifier = modifier.size(IconSize.Large.dp),
        tint = tint
    )
}

enum class IconSize(val dp: Dp) {
    Smallest(D600),
    Small(D800),
    Medium(D1000),
    Large(D1200),
    Largest(D1300),
}

@Preview(device = "spec:id=reference_phone,shape=Normal,width=1000,height=600,unit=dp,dpi=500")
@Composable
private fun IconPreview() {
    com.dangerfield.libraries.ui.preview.Preview(showBackground = true) {
        LazyColumn {
            items(IconSize.values()) {
                Icon(
                    iconSize = it,
                    podawanIcon = PodawanIcon.Check("check")
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${it.name} Icon", typography = PodawanTheme.typography.Body.B500
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}