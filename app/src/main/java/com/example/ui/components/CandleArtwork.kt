package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.CandleArtStyle
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.FlameAmber
import com.example.ui.theme.FlameBright
import com.example.ui.theme.PureWhite
import com.example.ui.theme.Terracotta
import com.example.ui.theme.WickCharcoal
import java.io.File

@Composable
fun BrandFlameEmblem(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Terracotta, ChampagneGold)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.56f)) {
            val w = this.size.width
            val h = this.size.height
            val s = minOf(w, h) / 24f

            val flamePath = Path().apply {
                moveTo(12f * s, 2f * s)
                cubicTo(12f * s, 2f * s, 7f * s, 7.5f * s, 7f * s, 12.5f * s)
                cubicTo(7f * s, 15.26f * s, 9.24f * s, 17.5f * s, 12f * s, 17.5f * s)
                cubicTo(14.76f * s, 17.5f * s, 17f * s, 15.26f * s, 17f * s, 12.5f * s)
                cubicTo(17f * s, 7.5f * s, 12f * s, 2f * s, 12f * s, 2f * s)
                close()
            }
            drawPath(flamePath, color = PureWhite.copy(alpha = 0.28f))
            drawPath(
                flamePath,
                color = PureWhite,
                style = Stroke(width = 2f * s, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            // Stem & Base
            drawLine(
                color = PureWhite,
                start = Offset(12f * s, 17.5f * s),
                end = Offset(12f * s, 21f * s),
                strokeWidth = 2f * s,
                cap = StrokeCap.Round
            )
            drawLine(
                color = PureWhite,
                start = Offset(9f * s, 21f * s),
                end = Offset(15f * s, 21f * s),
                strokeWidth = 2f * s,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun HeroDecorativeFlame(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val s = minOf(size.width, size.height) / 100f
        drawCircle(
            color = ChampagneGold.copy(alpha = 0.28f),
            radius = 45f * s,
            center = Offset(50f * s, 50f * s),
            style = Stroke(
                width = 1.6f * s,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.5f * s, 4.5f * s))
            )
        )
        val flame = Path().apply {
            moveTo(50f * s, 18f * s)
            cubicTo(50f * s, 18f * s, 34f * s, 36f * s, 34f * s, 52f * s)
            cubicTo(34f * s, 60.8f * s, 41.2f * s, 68f * s, 50f * s, 68f * s)
            cubicTo(58.8f * s, 68f * s, 66f * s, 60.8f * s, 66f * s, 52f * s)
            cubicTo(66f * s, 36f * s, 50f * s, 18f * s, 50f * s, 18f * s)
            close()
        }
        drawPath(flame, color = ChampagneGold.copy(alpha = 0.24f))
    }
}

@Composable
fun CandleArtworkBox(
    artStyleKey: String,
    customImagePath: String? = null,
    contentDescription: String,
    modifier: Modifier = Modifier,
    artworkSize: Dp = 112.dp,
    animateFlame: Boolean = true
) {
    val hasValidPhoto = !customImagePath.isNullOrBlank() && File(customImagePath).exists()

    if (hasValidPhoto) {
        AsyncImage(
            model = File(customImagePath!!),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
        return
    }

    val style = CandleArtStyle.fromKey(artStyleKey)
    val gradientColors = when (style) {
        CandleArtStyle.HEART -> listOf(Color(0xFFF9ECE4), Color(0xFFEED5C5))
        CandleArtStyle.SHELL -> listOf(Color(0xFFEAF3F5), Color(0xFFD8E7EC))
        CandleArtStyle.FLOATING -> listOf(Color(0xFFFBF0E6), Color(0xFFF3DEC9))
        CandleArtStyle.URLI -> listOf(Color(0xFFF9EFE0), Color(0xFFEAD4B0))
        CandleArtStyle.BOAT -> listOf(Color(0xFFF4EDE6), Color(0xFFE3D4C5))
        CandleArtStyle.HAMPER -> listOf(Color(0xFFF7EAE1), Color(0xFFE6CCB8))
        CandleArtStyle.JAR -> listOf(Color(0xFFF5EFE6), Color(0xFFE5D7C3))
    }

    val flickerProgress = if (animateFlame) {
        val infiniteTransition = rememberInfiniteTransition(label = "flameFlicker")
        val progress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "flamePulse"
        )
        progress
    } else {
        0.5f
    }

    Box(
        modifier = modifier.background(Brush.linearGradient(colors = gradientColors)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(artworkSize)) {
            val s = minOf(size.width, size.height) / 140f
            when (style) {
                CandleArtStyle.HEART -> drawHeartCandle(s, flickerProgress)
                CandleArtStyle.SHELL -> drawShellCandle(s, flickerProgress)
                CandleArtStyle.FLOATING -> drawFloatingCandle(s, flickerProgress)
                CandleArtStyle.URLI -> drawUrliCandle(s, flickerProgress)
                CandleArtStyle.BOAT -> drawBoatCandle(s, flickerProgress)
                CandleArtStyle.HAMPER -> drawHamperBox(s)
                CandleArtStyle.JAR -> drawJarCandle(s, flickerProgress)
            }
        }
    }
}

private fun DrawScope.drawAnimatedFlame(
    cx: Float,
    tipY: Float,
    baseY: Float,
    halfWidth: Float,
    flicker: Float
) {
    val scale = 0.96f + 0.08f * flicker
    val angle = -2.2f + 4.4f * flicker
    val pivot = Offset(cx, baseY)

    withTransform({
        rotate(degrees = angle, pivot = pivot)
        scale(scaleX = scale, scaleY = scale, pivot = pivot)
    }) {
        // Subtle warm halo
        drawCircle(
            color = FlameAmber.copy(alpha = 0.18f + 0.08f * flicker),
            radius = halfWidth * 1.9f,
            center = Offset(cx, (tipY + baseY) * 0.52f)
        )

        val outerPath = Path().apply {
            moveTo(cx, tipY)
            cubicTo(
                cx, tipY,
                cx - halfWidth, baseY - (baseY - tipY) * 0.34f,
                cx - halfWidth, baseY - (baseY - tipY) * 0.22f
            )
            cubicTo(
                cx - halfWidth, baseY + (baseY - tipY) * 0.04f,
                cx - halfWidth * 0.45f, baseY,
                cx, baseY
            )
            cubicTo(
                cx + halfWidth * 0.45f, baseY,
                cx + halfWidth, baseY + (baseY - tipY) * 0.04f,
                cx + halfWidth, baseY - (baseY - tipY) * 0.22f
            )
            cubicTo(
                cx + halfWidth, baseY - (baseY - tipY) * 0.34f,
                cx, tipY,
                cx, tipY
            )
            close()
        }
        drawPath(outerPath, color = FlameAmber)

        // Bright inner flame core
        val innerTipY = tipY + (baseY - tipY) * 0.38f
        val innerHalfW = halfWidth * 0.45f
        val innerPath = Path().apply {
            moveTo(cx, innerTipY)
            cubicTo(cx, innerTipY, cx - innerHalfW, baseY - (baseY - innerTipY) * 0.3f, cx, baseY - 1f)
            cubicTo(cx + innerHalfW, baseY - (baseY - innerTipY) * 0.3f, cx, innerTipY, cx, innerTipY)
            close()
        }
        drawPath(innerPath, color = FlameBright.copy(alpha = 0.9f))
    }
}

private fun DrawScope.drawHeartCandle(s: Float, flicker: Float) {
    // Ground shadow
    drawOval(
        color = Color(0xFFC87D55).copy(alpha = 0.2f),
        topLeft = Offset(26f * s, 108f * s),
        size = Size(88f * s, 16f * s)
    )
    // Outer porcelain heart vessel
    val outerHeart = Path().apply {
        moveTo(70f * s, 112f * s)
        cubicTo(70f * s, 112f * s, 24f * s, 84f * s, 24f * s, 52f * s)
        cubicTo(24f * s, 37f * s, 35f * s, 27f * s, 48f * s, 27f * s)
        cubicTo(57f * s, 27f * s, 65f * s, 32f * s, 70f * s, 39f * s)
        cubicTo(75f * s, 32f * s, 83f * s, 27f * s, 92f * s, 27f * s)
        cubicTo(105f * s, 27f * s, 116f * s, 37f * s, 116f * s, 52f * s)
        cubicTo(116f * s, 84f * s, 70f * s, 112f * s, 70f * s, 112f * s)
        close()
    }
    drawPath(outerHeart, color = Color(0xFFFFFDF9))
    drawPath(outerHeart, color = ChampagneGold, style = Stroke(width = 2.5f * s))

    // Inner soy wax fill
    val innerHeart = Path().apply {
        moveTo(70f * s, 105f * s)
        cubicTo(70f * s, 105f * s, 31f * s, 80f * s, 31f * s, 53f * s)
        cubicTo(31f * s, 42f * s, 39f * s, 34f * s, 49f * s, 34f * s)
        cubicTo(57f * s, 34f * s, 64f * s, 38f * s, 70f * s, 45f * s)
        cubicTo(76f * s, 38f * s, 83f * s, 34f * s, 91f * s, 34f * s)
        cubicTo(101f * s, 34f * s, 109f * s, 42f * s, 109f * s, 53f * s)
        cubicTo(109f * s, 80f * s, 70f * s, 105f * s, 70f * s, 105f * s)
        close()
    }
    drawPath(innerHeart, color = Color(0xFFF6E5D8))

    // Dual braided cotton wicks & flames
    drawLine(WickCharcoal, Offset(54f * s, 64f * s), Offset(54f * s, 53f * s), strokeWidth = 2.5f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(54f * s, 35f * s, 54f * s, 6f * s, flicker)

    drawLine(WickCharcoal, Offset(86f * s, 64f * s), Offset(86f * s, 53f * s), strokeWidth = 2.5f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(86f * s, 35f * s, 54f * s, 6f * s, 1f - flicker)
}

private fun DrawScope.drawShellCandle(s: Float, flicker: Float) {
    drawOval(
        color = Color(0xFF5B8591).copy(alpha = 0.2f),
        topLeft = Offset(25f * s, 108f * s),
        size = Size(90f * s, 16f * s)
    )
    // Conch Shell body
    val shell = Path().apply {
        moveTo(28f * s, 84f * s)
        cubicTo(26f * s, 62f * s, 45f * s, 42f * s, 72f * s, 42f * s)
        cubicTo(99f * s, 42f * s, 116f * s, 60f * s, 112f * s, 82f * s)
        cubicTo(108f * s, 100f * s, 90f * s, 108f * s, 70f * s, 108f * s)
        cubicTo(48f * s, 108f * s, 30f * s, 98f * s, 28f * s, 84f * s)
        close()
    }
    drawPath(shell, color = Color(0xFFFFFDFB))
    drawPath(shell, color = Color(0xFF9DBBC4), style = Stroke(width = 2.5f * s))

    // Translucent crystal gel wax pool
    drawOval(
        color = Color(0xFFBEE3EB).copy(alpha = 0.68f),
        topLeft = Offset(36f * s, 58f * s),
        size = Size(68f * s, 36f * s)
    )

    // Embedded pearls
    drawCircle(PureWhite, radius = 5f * s, center = Offset(52f * s, 78f * s))
    drawCircle(ChampagneGold, radius = 5f * s, center = Offset(52f * s, 78f * s), style = Stroke(1f * s))

    drawCircle(PureWhite, radius = 4.5f * s, center = Offset(88f * s, 80f * s))
    drawCircle(ChampagneGold, radius = 4.5f * s, center = Offset(88f * s, 80f * s), style = Stroke(1f * s))

    drawCircle(PureWhite, radius = 3.5f * s, center = Offset(62f * s, 86f * s))
    drawCircle(PureWhite, radius = 3.5f * s, center = Offset(79f * s, 71f * s))

    // Wick & Flame
    drawLine(WickCharcoal, Offset(70f * s, 74f * s), Offset(70f * s, 62f * s), strokeWidth = 2.5f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(70f * s, 42f * s, 64f * s, 7f * s, flicker)
}

private fun DrawScope.drawFloatingCandle(s: Float, flicker: Float) {
    // Water ripple base
    drawOval(
        color = Color(0xFFD8EBF0),
        topLeft = Offset(18f * s, 78f * s),
        size = Size(104f * s, 36f * s)
    )
    drawOval(
        color = Color(0xFFA6CCD6),
        topLeft = Offset(18f * s, 78f * s),
        size = Size(104f * s, 36f * s),
        style = Stroke(1.6f * s)
    )

    // Peony Flower Candle Left
    drawCircle(Color(0xFFF4A7A0), radius = 18f * s, center = Offset(52f * s, 84f * s))
    drawCircle(Color(0xFFFAD0C9), radius = 11f * s, center = Offset(52f * s, 84f * s))
    drawLine(WickCharcoal, Offset(52f * s, 82f * s), Offset(52f * s, 74f * s), strokeWidth = 2f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(52f * s, 61f * s, 75f * s, 4.4f * s, flicker)

    // Daisy Flower Candle Right
    drawCircle(Color(0xFFFFFDF7), radius = 16f * s, center = Offset(88f * s, 88f * s))
    drawCircle(Color(0xFFE6D5B8), radius = 16f * s, center = Offset(88f * s, 88f * s), style = Stroke(1.5f * s))
    drawCircle(Color(0xFFF2C14E), radius = 7f * s, center = Offset(88f * s, 88f * s))
    drawLine(WickCharcoal, Offset(88f * s, 86f * s), Offset(88f * s, 78f * s), strokeWidth = 2f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(88f * s, 65f * s, 79f * s, 4.4f * s, 1f - flicker)
}

private fun DrawScope.drawUrliCandle(s: Float, flicker: Float) {
    drawOval(
        color = Color(0xFF8C6239).copy(alpha = 0.22f),
        topLeft = Offset(24f * s, 107f * s),
        size = Size(92f * s, 16f * s)
    )
    // Golden Metal Urli Bowl
    val bowl = Path().apply {
        moveTo(24f * s, 72f * s)
        cubicTo(24f * s, 96f * s, 42f * s, 110f * s, 70f * s, 110f * s)
        cubicTo(98f * s, 110f * s, 116f * s, 96f * s, 116f * s, 72f * s)
        close()
    }
    drawPath(
        bowl,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFE7B95D), Color(0xFFD19A36), Color(0xFFA6741D)),
            start = Offset(24f * s, 72f * s),
            end = Offset(116f * s, 110f * s)
        )
    )
    drawPath(bowl, color = Color(0xFF9C6B21), style = Stroke(2f * s))

    // Scalloped Wax Rim
    drawOval(
        color = Color(0xFFFFFDF7),
        topLeft = Offset(24f * s, 60f * s),
        size = Size(92f * s, 24f * s)
    )
    drawOval(
        color = Color(0xFFB68029),
        topLeft = Offset(24f * s, 60f * s),
        size = Size(92f * s, 24f * s),
        style = Stroke(2.5f * s)
    )

    // Rose Petals
    drawCircle(Color(0xFFD96B66), radius = 4f * s, center = Offset(46f * s, 73f * s))
    drawCircle(Color(0xFFD96B66), radius = 4f * s, center = Offset(94f * s, 73f * s))

    // Dual Wicks & Flames
    drawLine(WickCharcoal, Offset(58f * s, 71f * s), Offset(58f * s, 61f * s), strokeWidth = 2f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(58f * s, 46f * s, 62.5f * s, 5f * s, flicker)

    drawLine(WickCharcoal, Offset(82f * s, 71f * s), Offset(82f * s, 61f * s), strokeWidth = 2f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(82f * s, 46f * s, 62.5f * s, 5f * s, 1f - flicker)
}

private fun DrawScope.drawBoatCandle(s: Float, flicker: Float) {
    drawOval(
        color = Color(0xFF5C4838).copy(alpha = 0.2f),
        topLeft = Offset(18f * s, 105f * s),
        size = Size(104f * s, 14f * s)
    )
    // Oval Boat Vessel
    val boat = Path().apply {
        moveTo(16f * s, 74f * s)
        cubicTo(22f * s, 98f * s, 44f * s, 106f * s, 70f * s, 106f * s)
        cubicTo(96f * s, 106f * s, 118f * s, 98f * s, 124f * s, 74f * s)
        close()
    }
    drawPath(boat, color = Color(0xFF8D6E53))
    drawPath(boat, color = Color(0xFF5E4632), style = Stroke(2f * s))

    drawOval(
        color = Color(0xFFFFFDF9),
        topLeft = Offset(16f * s, 62f * s),
        size = Size(108f * s, 24f * s)
    )
    drawOval(
        color = Color(0xFF6E523B),
        topLeft = Offset(16f * s, 62f * s),
        size = Size(108f * s, 24f * s),
        style = Stroke(2f * s)
    )

    // Triple Cotton Wicks
    drawLine(WickCharcoal, Offset(46f * s, 73f * s), Offset(46f * s, 63f * s), strokeWidth = 2f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(46f * s, 49f * s, 63.5f * s, 4.2f * s, flicker)

    drawLine(WickCharcoal, Offset(70f * s, 74f * s), Offset(70f * s, 63f * s), strokeWidth = 2f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(70f * s, 48f * s, 63f * s, 4.4f * s, 1f - flicker)

    drawLine(WickCharcoal, Offset(94f * s, 73f * s), Offset(94f * s, 63f * s), strokeWidth = 2f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(94f * s, 49f * s, 63.5f * s, 4.2f * s, flicker)
}

private fun DrawScope.drawHamperBox(s: Float) {
    // Shadow
    drawOval(
        color = Color(0xFF8C4F30).copy(alpha = 0.18f),
        topLeft = Offset(26f * s, 105f * s),
        size = Size(88f * s, 12f * s)
    )
    // Box Base
    drawRoundRect(
        color = Terracotta,
        topLeft = Offset(28f * s, 54f * s),
        size = Size(84f * s, 56f * s),
        cornerRadius = CornerRadius(10f * s, 10f * s)
    )
    drawRoundRect(
        color = Color(0xFF8C4F30),
        topLeft = Offset(28f * s, 54f * s),
        size = Size(84f * s, 56f * s),
        cornerRadius = CornerRadius(10f * s, 10f * s),
        style = Stroke(2f * s)
    )
    // Box Lid
    drawRoundRect(
        color = ChampagneGold,
        topLeft = Offset(24f * s, 44f * s),
        size = Size(92f * s, 14f * s),
        cornerRadius = CornerRadius(6f * s, 6f * s)
    )
    drawRoundRect(
        color = Color(0xFF8C4F30),
        topLeft = Offset(24f * s, 44f * s),
        size = Size(92f * s, 14f * s),
        cornerRadius = CornerRadius(6f * s, 6f * s),
        style = Stroke(2f * s)
    )
    // Satin Ribbon
    drawRect(
        color = Color(0xFFFAF7F2),
        topLeft = Offset(64f * s, 44f * s),
        size = Size(12f * s, 66f * s)
    )
    // Left Bow Loop
    val leftBow = Path().apply {
        moveTo(70f * s, 44f * s)
        cubicTo(70f * s, 44f * s, 52f * s, 28f * s, 46f * s, 36f * s)
        cubicTo(40f * s, 44f * s, 64f * s, 44f * s, 70f * s, 44f * s)
        close()
    }
    drawPath(leftBow, color = Color(0xFFFAF7F2))
    drawPath(leftBow, color = Color(0xFF8C4F30), style = Stroke(1.5f * s))

    // Right Bow Loop
    val rightBow = Path().apply {
        moveTo(70f * s, 44f * s)
        cubicTo(70f * s, 44f * s, 88f * s, 28f * s, 94f * s, 36f * s)
        cubicTo(100f * s, 44f * s, 76f * s, 44f * s, 70f * s, 44f * s)
        close()
    }
    drawPath(rightBow, color = Color(0xFFFAF7F2))
    drawPath(rightBow, color = Color(0xFF8C4F30), style = Stroke(1.5f * s))
}

private fun DrawScope.drawJarCandle(s: Float, flicker: Float) {
    drawOval(
        color = Color(0xFF4A382E).copy(alpha = 0.18f),
        topLeft = Offset(36f * s, 109f * s),
        size = Size(68f * s, 14f * s)
    )
    drawRoundRect(
        color = Color(0xFFFFFDF9),
        topLeft = Offset(42f * s, 52f * s),
        size = Size(56f * s, 60f * s),
        cornerRadius = CornerRadius(12f * s, 12f * s)
    )
    drawRoundRect(
        color = Terracotta,
        topLeft = Offset(42f * s, 52f * s),
        size = Size(56f * s, 60f * s),
        cornerRadius = CornerRadius(12f * s, 12f * s),
        style = Stroke(2.5f * s)
    )
    // Minimalist front label
    drawRoundRect(
        color = Color(0xFFFAF7F2),
        topLeft = Offset(48f * s, 68f * s),
        size = Size(44f * s, 28f * s),
        cornerRadius = CornerRadius(6f * s, 6f * s)
    )
    drawRoundRect(
        color = ChampagneGold,
        topLeft = Offset(48f * s, 68f * s),
        size = Size(44f * s, 28f * s),
        cornerRadius = CornerRadius(6f * s, 6f * s),
        style = Stroke(1.2f * s)
    )
    // Wick & Flame
    drawLine(WickCharcoal, Offset(70f * s, 52f * s), Offset(70f * s, 41f * s), strokeWidth = 2.5f * s, cap = StrokeCap.Round)
    drawAnimatedFlame(70f * s, 23f * s, 44f * s, 7f * s, flicker)
}
