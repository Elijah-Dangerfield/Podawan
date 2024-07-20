@file:Suppress("MagicNumber", "VariableNaming")

package com.dangerfield.libraries.ui.typography

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.TextUnit
import com.dangerfield.libraries.ui.Dimension
import com.dangerfield.libraries.ui.sp
import podawan.core.FontConfig

@Suppress("ComplexMethod")
data class TypographyResource internal constructor(
    internal val fontFamily: FontFamily,
    internal val fontWeight: FontWeight,
    internal val fontSize: TextUnit,
    internal val lineHeight: TextUnit,
    internal val lineBreak: LineBreak,
    internal val fontStyle: FontStyle = FontStyle.Normal,
    internal val identifier: String
) {

    val style: TextStyle = TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontStyle = fontStyle,
        lineBreak = lineBreak,
    )

    fun style(color: Color) = TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontStyle = fontStyle,
        lineBreak = lineBreak,
        color = color
    )

    val Italic: TypographyResource
        get() = TypographyResource(
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineBreak = lineBreak,
            fontStyle = FontStyle.Italic,
            identifier = "${identifier}-italic"
        )

    val Bold: TypographyResource
        get() = TypographyResource(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineBreak = lineBreak,
            fontStyle = fontStyle,
            identifier = "${identifier}-bold"
        )

    val ExtraBold: TypographyResource
        get() = TypographyResource(
            fontFamily = fontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineBreak = lineBreak,
            fontStyle = fontStyle,
            identifier = "${identifier}-extrabold"
        )

    val SemiBold: TypographyResource
        get() = TypographyResource(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineBreak = lineBreak,
            fontStyle = fontStyle,
            identifier = "${identifier}-semibold"
        )

    val Normal: TypographyResource
        get() = TypographyResource(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineBreak = lineBreak,
            fontStyle = fontStyle,
            identifier = "${identifier}-semibold"
        )

    val Thin: TypographyResource
        get() = TypographyResource(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Thin,
            fontSize = fontSize,
            lineHeight = lineHeight,
            lineBreak = lineBreak,
            fontStyle = fontStyle,
            identifier = "${identifier}-semibold"
        )
}

interface Typography {
    val Display: DisplayTypography
    val Heading: HeadingTypography
    val Body: BodyTypography
    val Label: LabelTypography
    val Default: TypographyResource
}

interface DisplayTypography {
    val D1500: TypographyResource
    val D1400: TypographyResource
    val D1300: TypographyResource
    val D1200: TypographyResource
    val D1100: TypographyResource
    val D1000: TypographyResource
    val D900: TypographyResource
    val D800: TypographyResource
}

interface HeadingTypography {
    val H1200: TypographyResource
    val H1100: TypographyResource
    val H1000: TypographyResource
    val H900: TypographyResource
    val H800: TypographyResource
    val H700: TypographyResource
    val H600: TypographyResource
    val H500: TypographyResource
    val H400: TypographyResource
}

interface LabelTypography {
    val L800: TypographyResource
    val L700: TypographyResource
    val L600: TypographyResource
    val L500: TypographyResource
    val L400: TypographyResource
}

interface BodyTypography {
    val B800: TypographyResource
    val B700: TypographyResource
    val B600: TypographyResource
    val B500: TypographyResource
    val B400: TypographyResource
}

class ConfiguredTypography(fontConfig: FontConfig) : Typography {
    private val headerFontFamily = when (fontConfig) {
        FontConfig.Default -> DefaultFontFamily
        is FontConfig.Custom -> fontConfig.headerFamily
    }

    private val bodyFamily = when (fontConfig) {
        FontConfig.Default -> DefaultFontFamily
        is FontConfig.Custom -> fontConfig.bodyFamily
    }

    private val labelFamily = when (fontConfig) {
        FontConfig.Default -> DefaultFontFamily
        is FontConfig.Custom -> fontConfig.labelFamily
    }

    private val displayFamily = when (fontConfig) {
        FontConfig.Default -> DefaultFontFamily
        is FontConfig.Custom -> fontConfig.displayFamily
    }

    override val Display = DisplayTypographyImpl(displayFamily)
    override val Heading = HeadingTypographyImpl(headerFontFamily)
    override val Body = BodyTypographyImpl(bodyFamily)
    override val Label = LabelTypographyImpl(labelFamily)
    override val Default = Heading.H800
}


class DisplayTypographyImpl(
    private val fontFamily: FontFamily
) : DisplayTypography {

    override val D1500 = TypographyResource(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D1500.sp(),
        lineHeight = Dimension.D1500.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-1500"
    )

    override val D1400 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D1400.sp(),
        lineHeight = Dimension.D1400.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-1400"
    )

    override val D1300 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D1300.sp(),
        lineHeight = Dimension.D1200.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-1300"
    )

    override val D1200 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D1200.sp(),
        lineHeight = Dimension.D1200.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-1100"
    )


    override val D1100 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D1100.sp(),
        lineHeight = Dimension.D1200.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-1100"
    )

    override val D1000 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D1000.sp(),
        lineHeight = Dimension.D1100.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-1000"
    )

    override val D900 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D900.sp(),
        lineHeight = Dimension.D1000.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-900"
    )

    override val D800 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = Dimension.D800.sp(),
        lineHeight = Dimension.D900.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "display-800"
    )
}

class HeadingTypographyImpl(
    private val fontFamily: FontFamily
) : HeadingTypography {

    override val H1200 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Dimension.D1200.sp(),
        lineHeight = Dimension.D1200.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-1200"
    )

    override val H1100 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Dimension.D1100.sp(),
        lineHeight = Dimension.D1100.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-1100"
    )

    override val H1000 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Dimension.D1000.sp(),
        lineHeight = Dimension.D1000.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-1000"
    )

    override val H900 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Dimension.D900.sp(),
        lineHeight = Dimension.D1000.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-900"
    )

    override val H800 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = Dimension.D800.sp(),
        lineHeight = Dimension.D900.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-800"
    )

    override val H700 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = Dimension.D700.sp(),
        lineHeight = Dimension.D800.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-700"
    )

    override val H600 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = Dimension.D600.sp(),
        lineHeight = Dimension.D700.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-600"
    )

    override val H500 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = Dimension.D500.sp(),
        lineHeight = Dimension.D700.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-500"
    )

    override val H400 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = Dimension.D400.sp(),
        lineHeight = Dimension.D700.sp(),
        lineBreak = LineBreak.Heading,
        identifier = "heading-400"
    )
}

class LabelTypographyImpl(
    private val fontFamily: FontFamily
) : com.dangerfield.libraries.ui.typography.LabelTypography {

    override val L800 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D800.sp(),
        lineHeight = Dimension.D900.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "label-800"
    )

    override val L700 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D700.sp(),
        lineHeight = Dimension.D900.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "label-700"
    )

    override val L600 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D600.sp(),
        lineHeight = Dimension.D600.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "label-600"
    )

    override val L500 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D500.sp(),
        lineHeight = Dimension.D500.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "label-500"
    )

    override val L400 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D400.sp(),
        lineHeight = Dimension.D500.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "label-400"
    )
}

class BodyTypographyImpl(
    private val fontFamily: FontFamily
) : com.dangerfield.libraries.ui.typography.BodyTypography {

    override val B800 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D800.sp(),
        lineHeight = Dimension.D900.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "body-800"
    )

    override val B700 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D700.sp(),
        lineHeight = Dimension.D900.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "body-700"
    )


    override val B600 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D600.sp(),
        lineHeight = Dimension.D800.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "body-600"
    )

    override val B500 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = Dimension.D500.sp(),
        lineHeight = Dimension.D700.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "body-500"
    )

    override val B400 = TypographyResource(
        fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = Dimension.D400.sp(),
        lineHeight = Dimension.D500.sp(),
        lineBreak = LineBreak.Paragraph,
        identifier = "body-400"
    )
}