package com.example.cityapiclient.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope

@OptIn(ExperimentalCoroutinesApi::class)
object GlobalTestScopes {/*
    val customScheduler = TestCoroutineScheduler()
    val ioDispatcher = StandardTestDispatcher(customScheduler)
    val scope = TestScope(ioDispatcher)*/
}