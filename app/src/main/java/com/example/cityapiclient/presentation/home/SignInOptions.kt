package com.example.cityapiclient.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.R
import com.example.cityapiclient.data.local.CurrentUser
import com.example.cityapiclient.presentation.components.AppIconButton
import com.example.cityapiclient.presentation.components.CardWithHeader
import com.example.cityapiclient.presentation.components.CustomIconButton
import com.example.cityapiclient.presentation.components.SubHeading
import com.example.cityapiclient.util.windowinfo.AppLayoutInfo
import com.example.cityapiclient.util.windowinfo.AppLayoutMode

@Composable
fun HomeSignInOrSignUp(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser,
    googleButton: @Composable () -> Unit,
    onSearchClicked: () -> Unit
) {
    if (appLayoutInfo.appLayoutMode != AppLayoutMode.FOLDED_SPLIT_TABLETOP
    ) {
        CardWithHeader(appLayoutInfo = appLayoutInfo,
            header = { SignUpHeading(appLayoutInfo = appLayoutInfo, currentUser) })
        {
            Spacer(modifier = Modifier.height(32.dp))
            googleButton()
            Spacer(modifier = Modifier.height(38.dp))
            SearchButton(onSearchClicked)
        }
    } else {
        SignUpHeading(appLayoutInfo = appLayoutInfo, currentUser)
        Spacer(modifier = Modifier.height(28.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.width(280.dp)
            ) {
                googleButton()
            }
            Column(
                modifier = Modifier.width(280.dp)
            ) {
                SearchButton(onSearchClicked)
            }
        }
    }
}

@Composable
private fun SearchButton(
    onSearchClicked: () -> Unit
) {
    CustomIconButton(
        buttonText = stringResource(R.string.city_name_search),
        onClick = onSearchClicked,
        imageRes = R.drawable.city_search,
        modifier = Modifier
    )
}

@Composable
private fun SignUpHeading(
    appLayoutInfo: AppLayoutInfo,
    currentUser: CurrentUser
) {
    when (currentUser) {
        is CurrentUser.SignedOutUser -> SubHeading(
            headingText = R.string.signed_out,
            appLayoutInfo = appLayoutInfo
        )
        else -> SubHeading(
            headingText = R.string.sign_up,
            appLayoutInfo = appLayoutInfo
        )
    }


}

