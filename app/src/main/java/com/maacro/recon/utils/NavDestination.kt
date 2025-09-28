package com.maacro.recon.utils

import androidx.compose.runtime.MutableState
import androidx.navigation.NavDestination

fun NavDestination?.orRemember(previous: MutableState<NavDestination?>): NavDestination? {
    return this?.also { previous.value = it } ?: previous.value
}