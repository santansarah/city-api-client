package com.example.cityapiclient.data.local

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * All of the data elements for an Onboarding screen.
 * This data comes from app resources.
 */
data class OnboardingScreen(
    val currentScreen: Int,
    @DrawableRes val headingIcon: Int,
    @StringRes val headingText: Int,
    @StringRes val subHeadingText: Int,
    @StringRes val cardHeading: Int,
    @StringRes val cardSubHeadingOne: Int,
    @StringRes val cardSubHeadingTwo: Int,
    val cardDetails: CardDetailsOption
)

/**
 * Card details can include an icon with text, or a row, for example
 * Population: 75000.
 */
sealed class CardDetailsOption {
    data class IconListItem(val options: List<Pair<Int, Int>>) : CardDetailsOption()
    data class RowItem(val options: List<Pair<Int, Int>>): CardDetailsOption()
}
