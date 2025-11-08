package com.mawmao.recon.forms.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.mawmao.recon.forms.model.annotations.FieldSpec

class FormGeneratorProcessor(private val codeGenerator: CodeGenerator) : SymbolProcessor {
    private val processor = FormAnnotationProcessor()
    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getAnnotatedPayloads().forEach { payload ->
            val model = createFormData(payload)
            FormCodeGenerator.generateFormFile(
                codeGenerator = codeGenerator,
                containingFile = payload.containingFile,
                data = model
            )
        }
        return emptyList()
    }


    private fun createFormData(payload: KSClassDeclaration): FormMeta {
        val formLabel = processor.getFormLabel(payload)
        val sectionSpecs = processor.getSectionsOrdered(payload)

        val groupedFields = payload.getAllProperties()
            .map { prop ->
                FieldMeta(
                    key = processor.getSerialName(prop),
                    label = processor.getFieldLabel(prop),
                    type = processor.getFieldType(prop),
                    options = processor.getFieldOptions(prop)
                ) to processor.getSectionId(prop)
            }
            .groupBy({ it.second }, { it.first })

        val elements = mutableListOf<FormElementMeta>()
        val processedGroups = mutableSetOf<String>()

        for (section in sectionSpecs) {
            val groupId = section.groupId

            if (groupId.isNotEmpty()) {
                if (!processedGroups.contains(groupId)) {
                    val groupedSections = sectionSpecs.filter { it.groupId == groupId }
                    val templateSections = groupedSections.map { sec ->
                        SectionMeta(
                            id = sec.id,
                            groupId = sec.groupId,
                            label = sec.label,
                            description = sec.description,
                            fields = groupedFields[sec.id] ?: emptyList()
                        )
                    }

                    val repeatable = RepeatableMeta(
                        groupId = groupId,
                        title = processor.getGroupTitle(payload, groupId),
                        templateSections = templateSections, // new property
                        sections = templateSections.toList()  // initial instance
                    )
                    elements.add(repeatable)
                    processedGroups.add(groupId)
                }
            } else {
                // Ungrouped section
                elements.add(
                    SectionMeta(
                        id = section.id,
                        label = section.label,
                        description = section.description,
                        fields = groupedFields[section.id] ?: emptyList()
                    )
                )
            }
        }

        return FormMeta(
            packageName = payload.packageName.asString(),
            name = "${payload.simpleName.asString()}Form",
            label = formLabel,
            elements = elements
        )
    }


    private fun Resolver.getAnnotatedPayloads(): List<KSClassDeclaration> =
        getSymbolsWithAnnotation(FieldSpec::class.qualifiedName!!)
            .filterIsInstance<KSPropertyDeclaration>()
            .mapNotNull { it.parentDeclaration as? KSClassDeclaration }
            .distinctBy { it.qualifiedName?.asString() }
            .toList()
}
