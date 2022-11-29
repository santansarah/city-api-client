package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    fieldValue: String = "",
    onChanged: (String) -> Unit,
    placeHolderValue: String? = null,
    readOnly: Boolean = false,
    label: String? = null,
    trailingIcon: @Composable () -> Unit = {},
    supportingText: String? = null
) {
    TextField(
        readOnly = readOnly,
        value = fieldValue,
        onValueChange = onChanged,
        placeholder = {
            placeHolderValue?.let {
                Text(
                    text = placeHolderValue,
                    //style = MaterialTheme.typography.bodyMedium,
                    //color = MaterialTheme.colorScheme.onPrimary.copy(.5f)
                )
            }
        },
        supportingText = {
            supportingText?.let {
                Text(text = supportingText)
            }
        },
        modifier = modifier,
        //textStyle = MaterialTheme.typography.bodyMedium,
        colors = textFieldColors(),
        trailingIcon = { trailingIcon() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIconAndClear(
    leadingIcon: @Composable () -> Unit,
    fieldValue: String = "",
    onChanged: (String) -> Unit,
    placeHolderValue: String,
    focusManager: FocusManager
) {
    TextField(
        value = fieldValue,
        onValueChange = onChanged,
        leadingIcon = { leadingIcon() },
        trailingIcon = { if (fieldValue.isNotBlank()) TrailingIcon(onChanged, focusManager) },
        placeholder = {
            Text(
                text = placeHolderValue,
                style = MaterialTheme.typography.titleMedium,
                //color = MaterialTheme.colorScheme.onPrimary.copy(.5f)
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.titleMedium,
        colors = textFieldColors()
    )
}

@Composable
fun TrailingIcon(
    resetField: (String) -> Unit,
    focusManager: FocusManager
) {
    IconButton(
        onClick = {
            resetField("")
            focusManager.clearFocus()
        }
    ) {
        Icon(
            Icons.Default.Clear,
            contentDescription = "Clear",
        )
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun textFieldColors() = TextFieldDefaults.textFieldColors(
    containerColor = MaterialTheme.colorScheme.surface,
    placeholderColor = MaterialTheme.colorScheme.outline,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
    focusedIndicatorColor = MaterialTheme.colorScheme.outline,
    cursorColor = MaterialTheme.colorScheme.onPrimary,
    focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
    textColor = MaterialTheme.colorScheme.onPrimary,
    focusedLabelColor = MaterialTheme.colorScheme.outline,
    unfocusedLabelColor = MaterialTheme.colorScheme.outline,
    disabledSupportingTextColor = MaterialTheme.colorScheme.onSecondary.copy(.5f),
    focusedSupportingTextColor = MaterialTheme.colorScheme.onSecondary.copy(.5f),
    unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSecondary.copy(.5f)
)