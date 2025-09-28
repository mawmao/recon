package com.maacro.recon.navigation

import com.maacro.recon.feature.home.HomeRoute
import com.maacro.recon.navigation.route
import kotlin.reflect.KClass

enum class ReconTopLevelDestination(val route: Any, val label: String) {
//    AUTH(route = AuthRoute, label = "Auth"),
    HOME(route = HomeRoute, label = "Home")
}

fun ReconTopLevelDestination.route(): KClass<*> = route::class