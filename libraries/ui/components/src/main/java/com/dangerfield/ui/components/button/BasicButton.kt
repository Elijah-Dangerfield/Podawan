package com.dangerfield.ui.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.Border
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.Elevation
import com.dangerfield.libraries.ui.Radii
import com.dangerfield.libraries.ui.StandardBorderWidth
import com.dangerfield.libraries.ui.bounceClick
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.ui.components.Surface
import com.dangerfield.ui.components.icon.SmallIcon
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.ProvideTextConfig
import com.dangerfield.ui.components.text.Text
import com.dangerfield.ui.components.text.TextConfig
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.thenIf

@Composable
internal fun BasicButton(
    backgroundColor: ColorResource?,
    borderColor: ColorResource?,
    contentColor: ColorResource,
    size: ButtonSize,
    style: ButtonStyle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: PodawanIcon? = null,
    contentPadding: PaddingValues = size.padding(hasIcon = icon != null),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.thenIf(enabled) {
                bounceClick(
                    mutableInteractionSource = interactionSource,
                    onClick = onClick
                )
            }
        ) {
            Surface(
                modifier = modifier
                    .semantics { role = Role.Button },
                radius = Radii.Button,
                elevation = if (backgroundColor != null) Elevation.Button else Elevation.None,
                color = backgroundColor,
                contentColor = contentColor,
                border = borderColor?.let { Border(it, OutlinedButtonBorderWidth) },
                contentPadding = contentPadding
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        ButtonIconSpacing,
                        Alignment.CenterHorizontally
                    )
                ) {
                    if (icon != null) {
                        SmallIcon(
                            podawanIcon = icon
                        )
                    }

                    ProvideTextConfig(size.textConfig(style), content = content)
                }
            }

        }
    }
}


@Composable
private fun ButtonSize.textConfig(style: ButtonStyle): TextConfig = when (this) {
    ButtonSize.Small -> when (style) {
        ButtonStyle.Background -> SmallButtonTextConfig
        ButtonStyle.NoBackground -> SmallTextButtonTextConfig
    }

    ButtonSize.Large -> when (style) {
        ButtonStyle.Background -> LargeButtonTextConfig
        ButtonStyle.NoBackground -> LargeTextButtonTextConfig
    }

    ButtonSize.ExtraSmall -> when (style) {
        ButtonStyle.Background -> ExtraSmallButtonTextConfig
        ButtonStyle.NoBackground -> ExtraSmallButtonTextConfig
    }
}

internal fun ButtonSize.padding(hasIcon: Boolean): PaddingValues =
    when (this) {
        ButtonSize.Small -> if (hasIcon) SmallButtonWithIconPadding else SmallButtonPadding
        ButtonSize.Large -> if (hasIcon) LargeButtonWithIconPadding else LargeButtonPadding
        ButtonSize.ExtraSmall -> if (hasIcon) ExtraSmallButtonWithIconPadding else ExtraSmallButtonPadding
    }

private val SmallButtonTextConfig: TextConfig @Composable get() = TextConfig(
    typography = PodawanTheme.typography.Label.L600,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
)

private val ExtraSmallButtonTextConfig: TextConfig @Composable get() = TextConfig(
    typography = PodawanTheme.typography.Label.L400,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
)

private val SmallTextButtonTextConfig: TextConfig @Composable get() = TextConfig(
    typography = PodawanTheme.typography.Body.B600.SemiBold,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
)

private val LargeButtonTextConfig: TextConfig @Composable get() = TextConfig(
    typography = PodawanTheme.typography.Label.L800.SemiBold,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
)


private val LargeButtonVerticalPadding = Dimension.D700
private val SmallButtonVerticalPadding = Dimension.D500
private val ExtraButtonVerticalPadding = Dimension.D500

private val LargeTextButtonTextConfig: TextConfig @Composable get() = TextConfig(
    typography = PodawanTheme.typography.Body.B700.SemiBold,
    overflow = TextOverflow.Ellipsis,
    maxLines = 1
)

private val LargeButtonPadding = PaddingValues(
    horizontal = Dimension.D900,
    vertical = LargeButtonVerticalPadding
)

private val LargeButtonWithIconPadding = PaddingValues(
    top = LargeButtonVerticalPadding,
    bottom = LargeButtonVerticalPadding,
    start = Dimension.D700,
    end = Dimension.D900,
)

private val SmallButtonPadding = PaddingValues(
    horizontal = Dimension.D800,
    vertical = SmallButtonVerticalPadding
)

private val SmallButtonWithIconPadding = PaddingValues(
    start = Dimension.D600,
    top = SmallButtonVerticalPadding,
    end = Dimension.D800,
    bottom = SmallButtonVerticalPadding
)

private val ExtraSmallButtonPadding = PaddingValues(
    horizontal = Dimension.D600,
    vertical = ExtraButtonVerticalPadding
)

private val ExtraSmallButtonWithIconPadding = PaddingValues(
    start = Dimension.D500,
    top = ExtraButtonVerticalPadding,
    end = Dimension.D500,
    bottom = ExtraButtonVerticalPadding
)

private val ButtonIconSpacing = Dimension.D200
private val OutlinedButtonBorderWidth = StandardBorderWidth

@Preview
@Composable
private fun LargeButton() {
    com.dangerfield.libraries.ui.preview.Preview {
        BasicButton(
            backgroundColor = PodawanTheme.colors.surfacePrimary,
            borderColor = null,
            contentColor = PodawanTheme.colors.onSurfacePrimary,
            size = ButtonSize.Large,
            style = ButtonStyle.Background,
            onClick = {},
            content = { Text(text = "Button") }
        )
    }
}

@Preview
@Composable
private fun SmallButton() {
    com.dangerfield.libraries.ui.preview.Preview {
        BasicButton(
            backgroundColor = PodawanTheme.colors.surfacePrimary,
            borderColor = null,
            contentColor = PodawanTheme.colors.onSurfacePrimary,
            size = ButtonSize.Small,
            style = ButtonStyle.Background,
            onClick = {},
            content = { Text(text = "Button") }
        )
    }
}

@Preview
@Composable
private fun ExtraSmallButton() {
    com.dangerfield.libraries.ui.preview.Preview {
        BasicButton(
            backgroundColor = PodawanTheme.colors.surfacePrimary,
            borderColor = null,
            contentColor = PodawanTheme.colors.onSurfacePrimary,
            size = ButtonSize.ExtraSmall,
            style = ButtonStyle.Background,
            onClick = {},
            content = { Text(text = "Button") }
        )
    }
}

@Preview
@Composable
private fun LargeButtonNOBackgroun() {
    com.dangerfield.libraries.ui.preview.Preview {
        BasicButton(
            backgroundColor = null,
            borderColor = null,
            contentColor = PodawanTheme.colors.onBackground,
            size = ButtonSize.Large,
            style = ButtonStyle.NoBackground,
            onClick = {},
            content = { Text(text = "Button") }
        )
    }
}