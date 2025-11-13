package com.mawmao.recon.forms.generator

import com.google.devtools.ksp.symbol.KSFile

/**
 * Metadata classes
 */

sealed interface FormElementMeta

data class FormMeta(
    val packageName: String,
    val name: String,
    val label: String,
    val elements: List<FormElementMeta>,
    val providers: List<String> = emptyList(),
    var factoryGenerated: Boolean = false,
    val containingFile: KSFile? = null
)

data class FieldMeta(
    val key: String,
    val label: String,
    val type: String,
    val options: List<String>? = null,
    val providerMeta: OptionsProviderMeta? = null,   // NEW
    val dependsOn: String = ""
)

data class OptionsProviderMeta(
    val providerFqcn: String,       // e.g. "com.my.app.generated.AllFormOptionsProvider"
    val methodName: String,         // e.g. "municipalityOptions"
    val takesParent: Boolean        // true if dependsOn != ""
)

data class SectionMeta(
    val id: String,
    val groupId: String = "",
    val label: String,
    val description: String,
    val fields: List<FieldMeta> = emptyList()
) : FormElementMeta

data class RepeatableMeta(
    val groupId: String,
    val title: String,
    val templateSections: List<SectionMeta>,
    val sections: List<SectionMeta> = templateSections
) : FormElementMeta
