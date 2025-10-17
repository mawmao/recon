package com.maacro.recon.feature.form.model

import kotlinx.serialization.Serializable

/**
 * TODO: convert to enum class in the future
 */

@Serializable
object FormType {
    const val PROFILE = "PROFILE"
    const val CULTURAL = "CULTURAL"
    const val NUTRIENT = "NUTRIENT"
    const val PRODUCTION = "PRODUCTION"
    const val MONITORING = "MONITORING"
    const val DAMAGE = "DAMAGE"
    const val UNKNOWN = "UNKNOWN"

    val coreTypes = listOf(PROFILE, CULTURAL, NUTRIENT, PRODUCTION)
    val optionalTypes = listOf(MONITORING, DAMAGE)
}
