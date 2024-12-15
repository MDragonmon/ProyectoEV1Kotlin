package com.shoko.letsgogambling.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import com.shoko.letsgogambling.R

// Define tu tipografía personalizada
val CustomFontFamily = FontFamily(
    Font(R.font.casino, FontWeight.Normal), // Fuente regular
    Font(R.font.casino3d, FontWeight.Bold)       // Fuente bold
)

// Configura una nueva tipografía
val CustomTypography = Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = CustomFontFamily,
        fontWeight = FontWeight.Bold
    )
    // Puedes añadir más estilos según lo necesites
)

// Colores
private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun LetsGoGamblingTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = CustomTypography, // Usa la tipografía personalizada
        content = content
    )
}
