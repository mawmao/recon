package com.mawmao.recon.forms.generator

/**
 * Metadata classes
 */

sealed interface FormElementMeta

data class FormMeta(
    val packageName: String,
    val name: String,
    val label: String,
    val elements: List<FormElementMeta>
)

data class FieldMeta(
    val key: String,
    val label: String,
    val type: String,
    val options: List<String>?
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
    val sectionTemplates: List<SectionMeta>
) : FormElementMeta
