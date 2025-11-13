package com.mawmao.recon.forms.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.mawmao.recon.forms.model.annotations.FieldSpec
import com.mawmao.recon.forms.model.annotations.FormTypeEnum

class FormGeneratorProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private val processor = FormAnnotationProcessor()
    private val generatedForms = mutableListOf<FormMeta>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val payloads = resolver.getAnnotatedPayloads()

        payloads.forEach { payload ->
            val model = createFormData(payload)
            generatedForms += model
            FormCodeGenerator.generateFormClassFile(
                codeGenerator = codeGenerator,
                file = payload.containingFile,
                metadata = model
            )
        }

        val formTypeEnum = resolver.getFormTypeEnum(logger)
        if (generatedForms.isNotEmpty()) {
            if (formTypeEnum != null) {
                val (containingFile, packageName) = generatedForms.firstNotNullOfOrNull {
                    it.containingFile?.let { file -> file to it.packageName }
                } ?: return emptyList()

                FormCodeGenerator.generateFormFactory(
                    codeGenerator = codeGenerator,
                    forms = generatedForms,
                    ftClass = formTypeEnum,
                    packageName = packageName,
                    file = containingFile
                )
                FormCodeGenerator.generateFormEntryPoint(
                    codeGenerator = codeGenerator,
                    packageName = packageName,
                    file = containingFile
                )

                generatedForms.clear()
                return emptyList()
            } else {
                logger.warn("Skipping FormFactory generation: @FormTypeEnum not found yet")
                return payloads
            }
        }

        return emptyList()
    }


    private fun createFormData(payload: KSClassDeclaration): FormMeta {
        val formLabel = processor.getFormLabel(payload)
        val sectionSpecs = processor.getSectionsOrdered(payload)

        val groupedFields = payload.getAllProperties()
            .map { prop ->
                val (staticOpts, providerMeta) = processor.getFieldOptions(prop)
                FieldMeta(
                    key = processor.getSerialName(prop),
                    label = processor.getFieldLabel(prop),
                    type = processor.getFieldType(prop),
                    options = staticOpts,
                    providerMeta = providerMeta,
                    dependsOn = processor.getFieldDependency(prop)
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
        val providers = groupedFields.values.flatten()
            .mapNotNull { it.providerMeta?.providerFqcn }
            .distinct()


        return FormMeta(
            packageName = payload.packageName.asString(),
            name = payload.simpleName.asString(),
            label = formLabel,
            elements = elements,
            providers = providers,
            containingFile = payload.containingFile
        )
    }

    private fun Resolver.getFormTypeEnum(logger: KSPLogger): KSClassDeclaration? {
        val annotated = getSymbolsWithAnnotation(FormTypeEnum::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()

        return annotated.firstOrNull { it.classKind == ClassKind.ENUM_CLASS }
    }

    private fun Resolver.getAnnotatedPayloads(): List<KSClassDeclaration> =
        getSymbolsWithAnnotation(FieldSpec::class.qualifiedName!!)
            .filterIsInstance<KSPropertyDeclaration>()
            .mapNotNull { it.parentDeclaration as? KSClassDeclaration }
            .distinctBy { it.qualifiedName?.asString() }
            .toList()
}
