package com.example.cityapiclient.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cityapiclient.presentation.theme.blueYellowGradient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppChip(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    labelText: String
) {
    AssistChip(
        modifier = Modifier,
        onClick = onClick,
        label = { Text(text = labelText) },
        leadingIcon = {
            icon()
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onPrimary.copy(.7f)
        ),
        /*border = AssistChipDefaults.assistChipBorder(
            MaterialTheme.colorScheme.onSurface.copy(.8f)
        )*/
    )
}