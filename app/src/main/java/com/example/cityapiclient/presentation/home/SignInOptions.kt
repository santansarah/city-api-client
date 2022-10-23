package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.presentation.components.AppCard
import com.example.cityapiclient.presentation.components.AppIconButton
import com.example.cityapiclient.presentation.components.OnboardingSubHeading
import com.example.cityapiclient.presentation.components.SubHeading
import com.example.cityapiclient.presentation.layouts.AppLayoutMode
import com.example.cityapiclient.presentation.layouts.DoubleScreenLayout

@Composable
fun HomeSignInOrSignUp(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser,
    googleButton: @Composable () -> Unit
) {
    SignUpHeading(appLayoutMode, currentUser)

    AppCard {
        if (appLayoutMode == AppLayoutMode.LANDSCAPE) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DoubleScreenLayout(leftContent = { googleButton() },
                    rightContent = { SearchButton() })
            }
        } else {
            googleButton()
            Spacer(modifier = Modifier.height(16.dp))
            SearchButton()
        }
    }
}

@Composable
private fun SearchButton() {
    AppIconButton(
        buttonText = "City Name Search",
        onClick = { /*TODO*/ },
        imageRes = Icons.Outlined.Search,
        modifier = Modifier
    )
}

@Composable
private fun SignUpHeading(
    appLayoutMode: AppLayoutMode,
    currentUser: CurrentUser
) {
    Spacer(modifier = Modifier.height(20.dp))

    when (currentUser) {
        is CurrentUser.UnAuthorizedUser -> SubHeading(headingText = 0,
            appLayoutMode = appLayoutMode,
            dynamicText = currentUser.error.message)
        is CurrentUser.SignedOutUser -> SubHeading(headingText = R.string.signed_out,
            appLayoutMode = appLayoutMode)
        else -> SubHeading(headingText = R.string.sign_up, appLayoutMode = appLayoutMode)
    }

}

