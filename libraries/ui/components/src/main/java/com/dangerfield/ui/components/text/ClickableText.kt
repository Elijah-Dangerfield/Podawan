package com.dangerfield.ui.components.text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.dangerfield.libraries.ui.ANNOTATED_STRING_URL_KEY
import com.dangerfield.libraries.ui.color.ColorResource
import com.dangerfield.libraries.ui.detectAndAnnotateLinks
import com.dangerfield.libraries.ui.theme.PodawanTheme
import com.dangerfield.libraries.ui.typography.TypographyResource


@NonRestartableComposable
@Composable
fun ClickableText(
    text: String,
    modifier: Modifier = Modifier,
    color: ColorResource? = null,
    lineBreak: LineBreak? = null,
    hyphens: Hyphens? = null,
    onClickAnnotatedText: (String) -> Unit = {},
    onClickUrl: (String) -> Unit = {},
    typography: TypographyResource = LocalTextConfig.current.typography
        ?: PodawanTheme.typography.Default,
    textDecoration: TextDecoration = LocalTextConfig.current.textDecoration ?: TextDecoration.None,
    textAlign: TextAlign? = LocalTextConfig.current.textAlign,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    overflow: TextOverflow = LocalTextConfig.current.overflow ?: DefaultTextOverflow,
    softWrap: Boolean = LocalTextConfig.current.softWrap ?: true,
    maxLines: Int = LocalTextConfig.current.maxLines ?: Int.MAX_VALUE,
) {
    val style = typography.toStyle(color, textDecoration, textAlign, hyphens, lineBreak)
    val annotatedText = text.processHtmlTags().detectAndAnnotateLinks()

    ClickableText(
        modifier = modifier,
        text = annotatedText,
        style = style,
        onTextLayout = onTextLayout,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
    ) { offset ->
        annotatedText.getStringAnnotations(offset, offset).firstOrNull()?.let { annotatedItem ->
            if (annotatedItem.tag == ANNOTATED_STRING_URL_KEY) {
                onClickUrl(annotatedItem.item)
            } else {
                onClickAnnotatedText(annotatedItem.item)
            }
        }
    }
}