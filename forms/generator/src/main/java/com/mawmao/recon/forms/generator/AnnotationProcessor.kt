package com.mawmao.recon.forms.generator

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.mawmao.recon.forms.model.FieldType
import com.mawmao.recon.forms.model.Section
import com.squareup.kotlinpoet.CodeBlock

class FormAnnotationProcessor {

    private fun findAnnotation(target: KSAnnotated, shortName: String): KSAnnotation? =
        target.annotations.firstOrNull { it.shortName.asString() == shortName }

    /**
     * Convenience annotation getters
     */
    private fun getFieldSpecAnnotation(prop: KSPropertyDeclaration): KSAnnotation? =
        findAnnotation(prop, "FieldSpec")

    private fun getOptionsAnnotation(prop: KSPropertyDeclaration): KSAnnotation? =
        findAnnotation(prop, "OptionsSpec")

    private fun getSerialNameAnnotation(clazz: KSPropertyDeclaration): KSAnnotation? =
        findAnnotation(clazz, "SerialName")

    private fun getFormSpecAnnotation(clazz: KSClassDeclaration): KSAnnotation? =
        findAnnotation(clazz, "FormSpec")


    @Suppress("UNCHECKED_CAST")
    fun getGroups(clazz: KSClassDeclaration): Map<String, String> {
        val formAnn = getFormSpecAnnotation(clazz) ?: return emptyMap()

        val rawGroups = formAnn.arguments
            .firstOrNull { it.name?.asString() == "groups" }
            ?.value as? List<KSAnnotation> ?: return emptyMap()

        return rawGroups.associate { ann ->
            val id = ann.arguments.firstOrNull { it.name?.asString() == "id" }?.value as? String ?: ""
            val title = ann.arguments.firstOrNull { it.name?.asString() == "title" }?.value as? String ?: ""
            id to title
        }
    }

    fun getGroupTitle(clazz: KSClassDeclaration, groupId: String): String =
        getGroups(clazz)[groupId] ?: groupId

    fun getFormLabel(clazz: KSClassDeclaration): String =
        getFormSpecAnnotation(clazz)
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "label" }
            ?.value as? String
            ?: clazz.simpleName.asString()


    @Suppress("UNCHECKED_CAST")
    fun getSectionsOrdered(clazz: KSClassDeclaration): List<SectionMeta> {
        val formAnn = getFormSpecAnnotation(clazz) ?: return emptyList()

        val rawSections = formAnn.arguments
            .firstOrNull { it.name?.asString() == "sections" }
            ?.value as? List<KSAnnotation> ?: return emptyList()

        return rawSections.map { ann ->
            val id = ann.arguments.firstOrNull { it.name?.asString() == "id" }?.value as? String ?: ""
            val groupId = ann.arguments.firstOrNull { it.name?.asString() == "groupId" }?.value as? String ?: ""
            val label = ann.arguments.firstOrNull { it.name?.asString() == "label" }?.value as? String ?: id
            val desc = ann.arguments.firstOrNull { it.name?.asString() == "description" }?.value as? String ?: ""
            SectionMeta(
                id = id,
                groupId = groupId,
                label = label,
                description = desc
            )
        }
    }

    /**
     * Property-level processors
     */
    fun getFieldLabel(prop: KSPropertyDeclaration): String =
        getFieldSpecAnnotation(prop)
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "label" }
            ?.value as? String
            ?: prop.simpleName.asString()

    fun getSerialName(prop: KSPropertyDeclaration): String {
        val serialName = getSerialNameAnnotation(prop)
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "value" }
            ?.value

        return serialName as? String ?: prop.simpleName.asString()
    }

    fun getSectionId(prop: KSPropertyDeclaration): String =
        getFieldSpecAnnotation(prop)
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "sectionId" }
            ?.value as? String
            ?: ""

    fun getFieldType(prop: KSPropertyDeclaration): String {
        val value = getFieldSpecAnnotation(prop)
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "fieldType" }
            ?.value

        val name = when (value) {
            is KSClassDeclaration -> value.simpleName.asString()
            else -> value?.toString() ?: "TEXT"
        }

        return when (name) {
            "DATE" -> CodeBlock.of("%T.DATE", FieldType::class)
            "NUMBER" -> CodeBlock.of("%T.NUMBER", FieldType::class)
            "DROPDOWN" -> CodeBlock.of("%T.DROPDOWN", FieldType::class)
            else -> CodeBlock.of("%T.TEXT", FieldType::class)
        }.toString()
    }

    @Suppress("UNCHECKED_CAST")
    fun getFieldOptions(prop: KSPropertyDeclaration): List<String> =
        getOptionsAnnotation(prop)
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "options" }
            ?.value as? List<String>
            ?: emptyList()
}
