@file:Suppress("MagicNumber")

package com.dangerfield.libraries.ui

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Immutable
class Radius private constructor(val shape: RoundedCornerShape) {
    internal constructor(cornerSize: CornerSize) : this(RoundedCornerShape(cornerSize))

    internal constructor(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ) : this(
        RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomEnd = bottomEnd,
            bottomStart = bottomStart
        )
    )

    internal constructor(
        topStart: CornerSize,
        topEnd: CornerSize,
    ) : this(
        RoundedCornerShape(
            topStart = topStart,
            topEnd = topEnd,
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        )
    )

    val cornerSize: CornerSize
        get() = shape.topStart.takeUnless { it == SquareCornerSize }
            ?: shape.topEnd.takeUnless { it == SquareCornerSize }
            ?: shape.bottomEnd.takeUnless { it == SquareCornerSize }
            ?: shape.bottomStart

    override fun equals(other: Any?): Boolean =
        this === other || other is Radius && shape == other.shape

    override fun hashCode(): Int = shape.hashCode()
    override fun toString(): String = "Radius(cornerSize=$cornerSize)"
}

object Radii {
    val Round = Radius(CornerSize(percent = 50))
    val R400 = Radius(CornerSize(DimensionResource.D400.dp))
    val None = Radius(SquareCornerSize)

    val Default get() = None
    val Button get() = Radius(CornerSize(DimensionResource.D500.dp))

    val BottomBar
        get() = Radius(
            topStart = CornerSize(DimensionResource.D400.dp),
            topEnd = CornerSize(DimensionResource.D400.dp),
            bottomEnd =  CornerSize(DimensionResource.D400.dp),
            bottomStart =  CornerSize(DimensionResource.D400.dp)
        )

    val IconButton get() = Round
    val Banner get() = R400
    val Header get() = None
    val Card get() = R400
}


fun Modifier.clip(radius: Radius): Modifier = clip(radius.shape)

private val SquareCornerSize = CornerSize(0.dp)


