package com.dangerfield.libraries.ui

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter

fun Drawable.toPainter(): Painter {
    if (this is BitmapDrawable) {
        return BitmapPainter(this.bitmap.asImageBitmap())
    }
    return AndroidDrawablePainter(drawable = this)
}

class AndroidDrawablePainter(val drawable: Drawable) : Painter() {
    override val intrinsicSize: Size
        get() = Size(
            width = drawable.intrinsicWidth.toFloat(),
            height = drawable.intrinsicHeight.toFloat()
        )

    override fun DrawScope.onDraw() {
        drawIntoCanvas { canvas ->
            drawable.setBounds(0, 0, size.width.toInt(), size.height.toInt())
            drawable.draw(canvas.nativeCanvas)
        }
    }
}