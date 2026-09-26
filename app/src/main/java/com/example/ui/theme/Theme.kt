package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = RailEmeraldPrimaryDark,
    onPrimary = RailEmeraldOnPrimaryDark,
    primaryContainer = RailEmeraldContainerDark,
    onPrimaryContainer = RailEmeraldOnContainerDark,
    secondary = RailCrimsonSecondaryDark,
    onSecondary = RailCrimsonOnSecondaryDark,
    secondaryContainer = RailCrimsonContainerDark,
    onSecondaryContainer = RailCrimsonOnContainerDark,
    tertiary = RailAmberTertiaryDark,
    onTertiary = RailAmberOnTertiaryDark,
    tertiaryContainer = RailAmberContainerDark,
    onTertiaryContainer = RailAmberOnContainerDark,
    background = RailBackgroundDark,
    onBackground = RailOnSurfaceDark,
    surface = RailSurfaceDark,
    onSurface = RailOnSurfaceDark,
    surfaceVariant = RailSurfaceVariantDark,
    onSurfaceVariant = RailOnSurfaceVariantDark,
    outline = RailOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = RailEmeraldPrimaryLight,
    onPrimary = RailEmeraldOnPrimaryLight,
    primaryContainer = RailEmeraldContainerLight,
    onPrimaryContainer = RailEmeraldOnContainerLight,
    secondary = RailCrimsonSecondaryLight,
    onSecondary = RailCrimsonOnSecondaryLight,
    secondaryContainer = RailCrimsonContainerLight,
    onSecondaryContainer = RailCrimsonOnContainerLight,
    tertiary = RailAmberTertiaryLight,
    onTertiary = RailAmberOnTertiaryLight,
    tertiaryContainer = RailAmberContainerLight,
    onTertiaryContainer = RailAmberOnContainerLight,
    background = RailBackgroundLight,
    onBackground = RailOnSurfaceLight,
    surface = RailSurfaceLight,
    onSurface = RailOnSurfaceLight,
    surfaceVariant = RailSurfaceVariantLight,
    onSurfaceVariant = RailOnSurfaceVariantLight,
    outline = RailOutlineLight
)

@Composable
fun BDRailTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    BDRailTrackerTheme(darkTheme = darkTheme, content = content)
}
