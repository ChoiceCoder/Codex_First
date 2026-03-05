package com.govtprep.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val Scheme = darkColorScheme(
    primary = Accent,
    background = Background,
    surface = Card,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onPrimary = TextPrimary
)

@Composable
fun GovtPrepTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Scheme,
        typography = Typography,
        content = content
    )
}
