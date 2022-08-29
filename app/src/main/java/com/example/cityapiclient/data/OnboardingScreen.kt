package com.example.cityapiclient.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

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

sealed class CardDetailsOption {
    data class IconListItem(val options: List<Pair<Int, Int>>) : CardDetailsOption()
    data class RowItem(val options: List<Pair<Int, Int>>): CardDetailsOption()
}
