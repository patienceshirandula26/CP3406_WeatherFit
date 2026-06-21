package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlueDark,
    onPrimary = OceanBlue,
    primaryContainer = SkyBlueDarkContainer,
    onPrimaryContainer = SkyBlueContainer,

    secondary = SunshineYellowDark,
    onSecondary = HoneyBrown,
    secondaryContainer = SunshineYellowDarkContainer,
    onSecondaryContainer = SunshineYellowContainer,

    tertiary = MintRainDark,
    onTertiary = DeepTeal,
    tertiaryContainer = MintRainDarkContainer,
    onTertiaryContainer = MintRainContainer,

    background = NightBackground,
    onBackground = MoonText,
    surface = NightSurface,
    onSurface = MoonText
)

private val LightColorScheme = lightColorScheme(
    primary = SkyBlue,
    onPrimary = CardWhite,
    primaryContainer = SkyBlueContainer,
    onPrimaryContainer = OceanBlue,

    secondary = HoneyBrown,
    onSecondary = CardWhite,
    secondaryContainer = SunshineYellowContainer,
    onSecondaryContainer = HoneyBrown,

    tertiary = MintRain,
    onTertiary = CardWhite,
    tertiaryContainer = MintRainContainer,
    onTertiaryContainer = DeepTeal,

    background = CreamBackground,
    onBackground = InkText,
    surface = CardWhite,
    onSurface = InkText
)

/**
 * App-wide Material 3 theme for WeatherFit. dynamicColor defaults to false
 * so the app always shows the hand-picked "Sunny Sky" palette that matches
 * the launcher icon, rather than the user's wallpaper colours.
 */
@Composable
fun WeatherFitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}