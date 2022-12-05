package com.example.cityapiclient.util

import android.content.Intent

fun getShareTextIntent(shareText: String) = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

