package com.example.booksrecommendation.ui.theme

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape

fun customNavBar(fabRadiusPx: Float, fabMarginPx: Float): Shape = GenericShape { size, _ ->
    val width = size.width
    val height = size.height

    addPath(Path().apply {
        moveTo(0f, 0f)
        lineTo(width / 2 - fabRadiusPx - fabMarginPx, 0f)

        arcTo(
            rect = Rect(
                left = width / 2 - fabRadiusPx - fabMarginPx,
                top = -fabRadiusPx,
                right = width / 2 + fabRadiusPx + fabMarginPx,
                bottom = fabRadiusPx
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )

        lineTo(width, 0f)
        lineTo(width, height)
        lineTo(0f, height)
        close()
    })
}
