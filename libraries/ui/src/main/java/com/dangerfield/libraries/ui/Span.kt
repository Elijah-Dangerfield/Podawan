package com.dangerfield.libraries.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.tooling.preview.Preview
import com.dangerfield.libraries.ui.theme.PodawanTheme
import podawan.core.throwIfDebug

const val ANNOTATED_STRING_URL_KEY = "URL"

fun String.makeBold(boldString: String): AnnotatedString = buildAnnotatedString {
    val startIndex = this@makeBold.indexOf(boldString)
    val endIndex = startIndex + boldString.length

    append(this@makeBold)

    if (startIndex < 0) {
        throwIfDebug(IllegalArgumentException("String ${this@makeBold} does not contain the specific text: $boldString"))
        return@buildAnnotatedString
    }

    addStyle(
        style = SpanStyle(fontWeight = FontWeight.W700),
        start = startIndex,
        end = endIndex
    )
    toAnnotatedString()
}

fun String.underline(underlinedString: String): AnnotatedString = buildAnnotatedString {
    val startIndex = this@underline.indexOf(underlinedString)
    val endIndex = startIndex + underlinedString.length

    append(this@underline)

    if (startIndex < 0) {
        throwIfDebug(IllegalArgumentException("String ${this@underline} does not contain the specific text: $underlinedString"))
        return@buildAnnotatedString
    }

    addStyle(
        style = SpanStyle(textDecoration = Underline),
        start = startIndex,
        end = endIndex
    )
    toAnnotatedString()
}

fun String.addStyle(stringToStyle: String, style: SpanStyle): AnnotatedString {
    val startIndex = this.indexOf(stringToStyle)
    val endIndex = startIndex + stringToStyle.length

    if (startIndex < 0) {
        throwIfDebug(IllegalArgumentException("String $this does not contain the specific text: $stringToStyle"))
        return buildAnnotatedString {
            append(this@addStyle)
        }
    }

    return buildAnnotatedString {
        append(this@addStyle)
        addStyle(
            style = style,
            start = startIndex,
            end = endIndex
        )
    }
}

@Composable
fun AnnotatedString.detectAndAnnotateLinks(
): AnnotatedString {
    val regex = Regex("""\b(?:https?://|www\.)\S+\b""")
    val matches = regex.findAll(this)

    val annotatedString = buildAnnotatedString {
        append(this@detectAndAnnotateLinks)
        matches.forEach {
            this.makeLookClickable(
                it.range,
                annotation = ANNOTATED_STRING_URL_KEY to it.value
            )
        }
    }

    return annotatedString
}

@Composable
fun String.addClickableUrl(
    linkText: String,
    url: String,
    style: SpanStyle = defaultStyle
) = makeLookClickable(linkText = linkText, style = style, annotation = ANNOTATED_STRING_URL_KEY to url)

@Composable
fun String.makeLookClickable(
    linkText: String,
    annotation: Pair<String, String>? = null,
    style: SpanStyle = defaultStyle
) = buildAnnotatedString {
    val startIndex = this@makeLookClickable.indexOf(linkText)
    val endIndex = startIndex + linkText.length

    append(this@makeLookClickable)

    if (startIndex < 0) {
        throwIfDebug(IllegalArgumentException("String ${this@makeLookClickable} does not contain the specific text: $linkText"))
        return@buildAnnotatedString
    }

    addStyle(
        style = style,
        start = startIndex,
        end = endIndex
    )
    if (annotation != null) {
        addStringAnnotation(
            tag = annotation.first,
            annotation = annotation.second,
            start = startIndex,
            end = endIndex
        )
    }
}


@Composable
fun AnnotatedString.Builder.makeLookClickable(
    linkRange: IntRange,
    annotation: Pair<String, String>? = null,
    style: SpanStyle = defaultStyle
)  {
    val startIndex = linkRange.first
    val endIndex = linkRange.last + 1

    addStyle(
        style = style,
        start = startIndex,
        end = endIndex
    )

    if (annotation != null) {
        addStringAnnotation(
            tag = annotation.first,
            annotation = annotation.second,
            start = startIndex,
            end = endIndex
        )
    }
}

@Composable
fun AnnotatedString.makeLookClickable(
    linkText: String,
    annotation: Pair<String, String>? = null,
    style: SpanStyle = defaultStyle
) = buildAnnotatedString {
    val startIndex = this@makeLookClickable.indexOf(linkText)
    val endIndex = startIndex + linkText.length

    append(this@makeLookClickable)

    if (startIndex < 0) {
        throwIfDebug(IllegalArgumentException("String ${this@makeLookClickable} does not contain the specific text: $linkText"))
        return@buildAnnotatedString
    }

    addStyle(
        style = style,
        start = startIndex,
        end = endIndex
    )
    if (annotation != null) {
        addStringAnnotation(
            tag = annotation.first,
            annotation = annotation.second,
            start = startIndex,
            end = endIndex
        )
    }
}

private val defaultStyle: SpanStyle
    @Composable
    @ReadOnlyComposable
    get() = SpanStyle(
        color = PodawanTheme.colors.text.color,
        fontWeight = FontWeight.ExtraBold,
        textDecoration = Underline
    )

@Preview()
@Composable
private fun MakeLinkPreview() {
    Preview {

        Column {
            val makeLink = "This is some random text. But this text is clickable".makeLookClickable(
                "But this text is clickable"
            )
            val makeURL =
                "makeWebLinkClickable: This is normal content, but this is a clickable url".addClickableUrl(
                    "clickable url",
                    "http://www.thisisurl.com"
                )

            Text(makeLink)

            VerticalSpacerD800()

            Text(makeURL)

        }
    }
}
