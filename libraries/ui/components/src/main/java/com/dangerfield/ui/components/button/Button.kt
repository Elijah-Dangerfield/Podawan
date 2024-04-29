package com.dangerfield.ui.components.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.color.animateColorResourceAsState
import com.dangerfield.ui.components.icon.PodawanIcon
import com.dangerfield.ui.components.text.Text
import com.dangerfield.libraries.ui.theme.PodawanTheme

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: PodawanIcon? = null,
    type: ButtonType = LocalButtonType.current,
    size: ButtonSize = LocalButtonSize.current,
    style: ButtonStyle = LocalButtonStyle.current,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val backgroundColor = backgroundColor(type, style, enabled)?.let { targetColor ->
        key(type, style) {
            animateColorResourceAsState(
                targetValue = targetColor,
                label = "Background_Color_Anim"
            )
        }.value
    }
    val contentColor by key(type, style) {
        animateColorResourceAsState(
            targetValue = type.contentColor(style, enabled),
            label = "Content_Color_Anim"
        )
    }

    val borderColor = when (type) {
        ButtonType.Primary -> null
        ButtonType.Secondary -> PodawanTheme.colors.border
    }

    BasicButton(
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        contentColor = contentColor,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        size = size,
        style = style,
        enabled = enabled,
        interactionSource = interactionSource,
        content = content
    )
}

enum class ButtonType {
    Primary,
    Secondary,
}

enum class ButtonSize {
    Large,
    Small,
    ExtraSmall
}

enum class ButtonStyle {
    Background,
    NoBackground,
}

@Composable
fun ProvideButtonConfig(
    type: ButtonType = LocalButtonType.current,
    size: ButtonSize = LocalButtonSize.current,
    style: ButtonStyle = LocalButtonStyle.current,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalButtonType provides type,
        LocalButtonSize provides size,
        LocalButtonStyle provides style,
        content = content
    )
}

private val LocalButtonType = compositionLocalOf { ButtonType.Primary }
internal val LocalButtonSize = compositionLocalOf { ButtonSize.Large }
private val LocalButtonStyle = compositionLocalOf { ButtonStyle.Background }

@Composable
@ReadOnlyComposable
private fun backgroundColor(
    type: ButtonType,
    style: ButtonStyle,
    enabled: Boolean,
): ColorResource? = when (style) {
    ButtonStyle.Background -> type.filledBackgroundColorToken(enabled)
    ButtonStyle.NoBackground -> null
}

@Composable
@ReadOnlyComposable
private fun ButtonType.filledBackgroundColorToken(enabled: Boolean) = when {
    !enabled -> PodawanTheme.colors.surfaceDisabled
    else -> when (this) {
        ButtonType.Primary -> PodawanTheme.colors.surfacePrimary
        ButtonType.Secondary -> null
    }
}

@Composable
@ReadOnlyComposable
private fun ButtonType.contentColor(style: ButtonStyle, enabled: Boolean): ColorResource =
    when (style) {
        ButtonStyle.Background -> when {
            !enabled -> PodawanTheme.colors.onSurfaceDisabled
            else -> when (this) {
                ButtonType.Primary -> PodawanTheme.colors.onSurfacePrimary
                ButtonType.Secondary -> PodawanTheme.colors.onSurfaceSecondary
            }
        }

        ButtonStyle.NoBackground -> when {
            !enabled -> PodawanTheme.colors.textDisabled
            else -> when (this) {
                ButtonType.Primary -> PodawanTheme.colors.onBackground
                ButtonType.Secondary -> PodawanTheme.colors.onBackground
            }
        }
    }


@Preview
@Composable
private fun PreviewPrimaryButton() {
    com.dangerfield.libraries.ui.preview.Preview {
        ProvideButtonConfig(type = ButtonType.Primary, size = ButtonSize.Large) {
            Button(
                onClick = {},
                content = { Text("Primary Button") }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPrimaryButtonDixabled() {
    com.dangerfield.libraries.ui.preview.Preview {
        ProvideButtonConfig(type = ButtonType.Primary, size = ButtonSize.Large) {
            Button(
                enabled = false,
                onClick = {},
                content = { Text("Primary Button") }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSecondaryButton() {
    com.dangerfield.libraries.ui.preview.Preview {
        ProvideButtonConfig(type = ButtonType.Secondary, size = ButtonSize.Large) {
            Button(
                onClick = {},
                content = { Text("Secondary Button") }
            )
        }
    }
}

