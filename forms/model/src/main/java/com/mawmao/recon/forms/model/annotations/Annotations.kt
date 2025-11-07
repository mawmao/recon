package com.mawmao.recon.forms.model.annotations

import com.mawmao.recon.forms.model.FieldType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class FormSpec(
    val label: String,
    val sections: Array<SectionSpec>,
    val groups: Array<GroupSpec> = []
)

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SectionSpec(
    val id: String,
    val groupId: String = "",
    val label: String,
    val description: String = ""
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class FieldSpec(
    val label: String = "",
    val fieldType: FieldType = FieldType.TEXT,
    val placeholder: String = "",
    val sectionId: String = ""
)

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class GroupSpec(
    val id: String,
    val title: String,
    val repeatable: Boolean = true,
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class OptionsSpec(val options: Array<String>)
