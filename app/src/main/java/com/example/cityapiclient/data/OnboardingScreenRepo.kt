package com.example.cityapiclient.data

import com.example.cityapiclient.R

object OnboardingScreenRepo {
    fun getScreens() = onBoardingScreens
}

/**
 * Construct the Onboarding screen data.
 */
private val onBoardingScreens = listOf(

    OnboardingScreen(
        1,
        R.drawable.cityneon,
        R.string.onboarding_city_header,
        R.string.onboarding_city_subHeading,
        R.string.onboarding_city_cardHeading,
        R.string.onboarding_city_cardSubHeadingOne,
        R.string.onboarding_city_cardSubHeadingTwo,
        CardDetailsOption.RowItem(
            listOf(
                Pair(
                    R.string.onboarding_city_population,
                    R.string.onboarding_city_populationNum),
                Pair(
                    R.string.onboarding_city_lat,
                    R.string.onboarding_city_latNum
                ),
                Pair(
                    R.string.onboarding_city_long,
                    R.string.onboarding_city_longNum
                )
            )
        )
    ),
    OnboardingScreen(
        2,
        R.drawable.search_icon,
        R.string.onboarding_search_header,
        R.string.onboarding_search_subHeading,
        R.string.onboarding_search_cardHeading,
        R.string.onboarding_search_cardSubHeadingOne,
        R.string.onboarding_search_cardSubHeadingTwo,
        CardDetailsOption.IconListItem(
            listOf(
                Pair(
                    R.drawable.prefix_check,
                    R.string.onboarding_search_cityPrefix
                ),
                Pair(
                    R.drawable.prefix_check,
                    R.string.onboarding_search_zipPrefix
                ),
                Pair(
                    R.drawable.prefix_check,
                    R.string.onboarding_search_countyPrefix
                ),
            )
        )
    )

)
