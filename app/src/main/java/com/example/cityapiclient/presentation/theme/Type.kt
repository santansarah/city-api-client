package com.example.cityapiclient.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.cityapiclient.R


@OptIn(ExperimentalTextApi::class)
val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

@OptIn(ExperimentalTextApi::class)
val titleFont = GoogleFont(name = "Bangers")

@OptIn(ExperimentalTextApi::class)
val AssistantFont = GoogleFont(name = "Assistant")

@OptIn(ExperimentalTextApi::class)
val titleFamily = FontFamily(
    Font(googleFont = titleFont, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    //Font(resId = R.font.assistant_regular, weight = FontWeight.Normal),
)


@OptIn(ExperimentalTextApi::class)
val fontFamily = FontFamily(
    Font(googleFont = AssistantFont, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    //Font(resId = R.font.assistant_regular, weight = FontWeight.Normal),

    Font(googleFont = AssistantFont, fontProvider = googleFontProvider, weight = FontWeight.Light),
    //Font(resId = R.font.assisstant_light, weight = FontWeight.Light),

    Font(googleFont = AssistantFont, fontProvider = googleFontProvider, weight = FontWeight.Medium),
    /*Font(
        resId = R.font.assisstant_medium,
        weight = FontWeight.Medium
    ),*/

    Font(
        googleFont = AssistantFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.SemiBold
    ),
    /*Font(
        resId = R.font.assistant_semibold,
        weight = FontWeight.SemiBold
    ),*/

    Font(
        googleFont = AssistantFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.Bold
    ),
    /*Font(
        resId = R.font.assisstant_bold,
        weight = FontWeight.Bold
    ),*/

    Font(
        googleFont = AssistantFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.ExtraBold
    ),
/*
    Font(
        resId = R.font.assistant_extrabold,
        weight = FontWeight.ExtraBold
    ),
*/

    Font(
        googleFont = AssistantFont,
        fontProvider = googleFontProvider,
        weight = FontWeight.ExtraLight
    ),
    /*Font(
        resId = R.font.assistant_extralight,
        weight = FontWeight.ExtraLight
    ),*/
)


val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = titleFamily,
        fontWeight = FontWeight.W400,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = titleFamily,
        fontWeight = FontWeight.W400,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),

    titleMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
