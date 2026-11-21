package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val DiyansLightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = PureWhite,
    primaryContainer = Sand,
    onPrimaryContainer = Charcoal,
    secondary = ChampagneGold,
    onSecondary = Charcoal,
    secondaryContainer = ChampagneGoldSoft,
    onSecondaryContainer = Charcoal,
    tertiary = Charcoal,
    onTertiary = Cream,
    background = Cream,
    onBackground = Charcoal,
    surface = PureWhite,
    onSurface = Charcoal,
    surfaceVariant = Sand,
    onSurfaceVariant = WarmGray,
    outline = ChampagneGold,
    outlineVariant = WarmBorder,
    error = SoftErrorText,
    onError = PureWhite,
    errorContainer = SoftErrorBg,
    onErrorContainer = SoftErrorText
)

val DiyansShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun DiyansCandleTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DiyansLightColorScheme,
        typography = DiyansTypography,
        shapes = DiyansShapes,
        content = content
    )
}
