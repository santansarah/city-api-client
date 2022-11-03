package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithIconAndClear(
    leadingIcon: @Composable () -> Unit,
    fieldValue: String = "",
    onChanged: (String) -> Unit,
    placeHolderValue: String
) {
    TextField(
        value = fieldValue,
        onValueChange = onChanged,
        leadingIcon = { leadingIcon() },
        trailingIcon = { if (fieldValue.isNotBlank()) TrailingIcon(onChanged) },
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
    resetField: (String) -> Unit
) {
    IconButton(
        onClick = { resetField("") }
    ) {
        Icon(
            Icons.Default.Clear,
            contentDescription = "Clear",
        )
    }
}



@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun textFieldColors() = TextFieldDefaults.textFieldColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    placeholderColor = MaterialTheme.colorScheme.outline,
    unfocusedLeadingIconColor = MaterialTheme.colorScheme.outline,
    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
    focusedIndicatorColor = MaterialTheme.colorScheme.outline,
    cursorColor = MaterialTheme.colorScheme.onPrimary,
    focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
    textColor = MaterialTheme.colorScheme.onPrimary
)