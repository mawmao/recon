package com.maacro.recon.feature.form.data.registry.util

/**
 * Should be used only for strings made with `FormType.name`
 */
fun String.toActivityType() = this.lowercase().replace("_", "-")