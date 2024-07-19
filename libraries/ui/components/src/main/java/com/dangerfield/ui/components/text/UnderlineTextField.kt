package com.dangerfield.ui.components.text

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.size.Dimension
import com.dangerfield.libraries.ui.Dimension.D200
import com.dangerfield.libraries.ui.Dimension.D700
import com.dangerfield.libraries.ui.Dimension.D800
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.typography.TypographyResource

@Composable
fun UnderlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    typographyToken: TypographyResource = LocalTextConfig.current.typography
        ?: PodawanTheme.typography.Default,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    color: Color = PodawanTheme.colors.onBackground.color,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    cursorBrush: Brush = SolidColor(PodawanTheme.colors.accent.color)
) {

    Column(Modifier.width(IntrinsicSize.Max)) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            enabled = enabled,
            readOnly = readOnly,
            typographyToken = typographyToken,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            color = color,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    isError = isError,
                    label = label,
                    placeholder = {
                        ProvideTextConfig(
                            config = TextConfig(
                                typography = typographyToken.copy(
                                    fontWeight = FontWeight.ExtraLight
                                ),
                                color = PodawanTheme.colors.textDisabled,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Start,
                                maxLines = 1,
                            )
                        ) {
                            placeholder?.invoke()
                        }
                    },
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    supportingText = supportingText,
                    colors = outlinedTextFieldColors,
                    contentPadding = underlineTextFieldPadding,
                    container = {},
                )
            }
        )

        HorizontalDivider(
            color = PodawanTheme.colors.borderDisabled.color,
            thickness = 3.dp
        )
    }
}

private val underlineTextFieldPadding
    @Composable
    get() = PaddingValues(D700, D700, D700, D200)

private val outlinedTextFieldColors
    @Composable
    get() = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = PodawanTheme.colors.background.color,
        unfocusedContainerColor = PodawanTheme.colors.background.color,
        disabledContainerColor = PodawanTheme.colors.background.color,
        focusedBorderColor = PodawanTheme.colors.border.color,
        unfocusedBorderColor = PodawanTheme.colors.border.color,
        disabledBorderColor = PodawanTheme.colors.borderDisabled.color,
    )
@Composable
@Preview
private fun PreviewOutlinedTextField() {
    com.dangerfield.libraries.ui.preview.Preview(modifier = Modifier.padding(24.dp)) {
        UnderlineTextField(value = "Hello World", onValueChange = { })
    }
}

@Composable
@Preview
private fun PreviewOutlinedTextFieldError() {
    com.dangerfield.libraries.ui.preview.Preview(modifier = Modifier.padding(24.dp)) {
        UnderlineTextField(
            value = "Hello World",
            isError = true,
            onValueChange = { }
        )
    }
}

@Composable
@Preview
private fun PreviewOutlinedTextFieldDisabled() {
    com.dangerfield.libraries.ui.preview.Preview(modifier = Modifier.padding(24.dp)) {
        UnderlineTextField(
            value = "Hello World",
            enabled = false,
            onValueChange = { }
        )
    }
}

@Composable
@Preview
private fun PreviewOutlinedTextField1() {
    com.dangerfield.libraries.ui.preview.Preview(modifier = Modifier.padding(24.dp)) {
        UnderlineTextField(
            value = "1",
            onValueChange = { },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.width(IntrinsicSize.Max)
        )
    }
}

@Composable
@Preview
private fun PreviewOutlinedTextFieldEmpty() {
    com.dangerfield.libraries.ui.preview.Preview(modifier = Modifier.padding(24.dp)) {
        UnderlineTextField(value = "", onValueChange = { }, placeholder = { Text("Type something") })
    }
}
