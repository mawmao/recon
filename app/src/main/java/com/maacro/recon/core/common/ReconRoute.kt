package com.maacro.recon.core.common

interface ReconRoute

inline fun <reified T : Any> navRoute(): String = T::class.qualifiedName ?: "Unknown"
fun navRoute(any: Any): String = any::class.qualifiedName ?: "Unknown"

