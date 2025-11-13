package com.mawmao.recon.forms.model.annotations

import com.mawmao.recon.forms.model.FieldType
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class FormSpec(
    val label: String,
    val sections: Array<SectionSpec>,
    val groups: Array<GroupSpec> = []
)


// Used in [FormSpec]
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SectionSpec(
    val id: String,
    val groupId: String = "",
    val label: String,
    val description: String = ""
)


// Used in [FormSpec]
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class GroupSpec(
    val id: String,
    val title: String,
    val repeatable: Boolean = true,
)


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class FieldSpec(
    val label: String = "",
    val fieldType: FieldType = FieldType.TEXT,
    val placeholder: String = "",
    val sectionId: String = "",
    val dependsOn: String = ""
)


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class DataSpec(
    val staticData: Array<String> = [],
    val repositoryClass: KClass<*> = Nothing::class,
    val fetchFunction: String = "",
    val parent: String = ""
)


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class OptionsSpec(
    val options: Array<String> = [],        // static options
    val displayColumn: String = "name",
    val valueColumn: String = "id",
    val repoClass: KClass<*> = Nothing::class,
    val fetchAllFunction: String = "getAll",
    val fetchByParentFunction: String = "getByParentId"
)


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class FormTypeEnum
