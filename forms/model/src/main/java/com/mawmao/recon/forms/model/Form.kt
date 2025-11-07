package com.mawmao.recon.forms.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface FormElement

@Serializable
data class Form(
    val label: String,
    val elements: List<FormElement>
) {
    val totalSections: Int
        get() = sections.size

    val sections: List<Section>
        get() = elements.flatMap { element ->
            when (element) {
                is Section -> listOf(element)
                is Repeatable -> {
                    val templateSections = element.sectionTemplates
                    val instanceSections = element.instances.flatten()
                    templateSections + instanceSections
                }
            }
        }

}

fun Form.sectionWrappers(): List<SectionWrapper> = elements.flatMap { element ->
    when (element) {
        is Section -> listOf(SectionWrapper(section = element))
        is Repeatable -> {
            val templates = element.sectionTemplates.map { SectionWrapper(it, element) }
            val instances = element.instances.flatMapIndexed { index, instanceSections ->
                instanceSections.map { SectionWrapper(it, element, index) }
            }
            templates + instances
        }
    }
}


fun Repeatable.instantiateSections(instanceIndex: Int): List<Section> {
    return sectionTemplates.map { template ->
        val newFields = template.fields.map { field ->
            field.copy(key = "${this.groupId}_${instanceIndex}_${field.key}")
        }
        // give section a unique key so Section equality is stable
        template.copy(
            key = "${template.key}_${this.groupId}_$instanceIndex",
            fields = newFields
        )
    }
}

fun Form.withUpdatedRepeatable(updated: Repeatable): Form {
    val newElements = elements.map { e -> if (e is Repeatable && e.groupId == updated.groupId) updated else e }
    return this.copy(elements = newElements)
}

@Serializable
data class SectionWrapper(
    val section: Section,
    val parentRepeatable: Repeatable? = null,
    val instanceIndex: Int? = null
)

@Serializable
data class Section(
    val key: String,
    val title: String,
    val description: String,
    val fields: List<Field>
) : FormElement

@Serializable
data class Repeatable(
    val groupId: String,
    val title: String,
    val sectionTemplates: List<Section>,
    val instances: List<List<Section>> = emptyList()
) : FormElement

@Serializable
data class Field(
    val key: String,
    val label: String,
    val type: FieldType,
    val required: Boolean = true,
    val options: List<String>? = null
)

@Serializable
enum class FieldType {
    TEXT, NUMBER, DATE, DROPDOWN, CHECKBOX
}

