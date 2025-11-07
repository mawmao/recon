package com.mawmao.recon.forms.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSFile
import com.mawmao.recon.forms.model.Field;
import com.mawmao.recon.forms.model.Form
import com.mawmao.recon.forms.model.Repeatable
import com.mawmao.recon.forms.model.Section
import com.squareup.kotlinpoet.CodeBlock;
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.writeTo

object FormCodeGenerator {

    fun generateFormFile(
        codeGenerator: CodeGenerator,
        containingFile: KSFile?,
        data: FormMeta
    ) {
        if (containingFile == null) return

        val file = FileSpec.builder(data.packageName, data.name)
            .addProperty( PropertySpec
                    .builder(data.name, Form::class)
                    .initializer(generateForm(data))
                    .build()
            )
            .build()

        file.writeTo(codeGenerator, Dependencies(false, containingFile))
    }

    private fun generateForm(model: FormMeta): CodeBlock = CodeBlock.builder().apply {
        add("%T(\n", Form::class)
        doubleIndent()
        add("label = %S,\n", model.label)
        add("elements = listOf(\n")
        doubleIndent()
        add(model.elements.joinToCode(",\n") { elem ->
            when (elem) {
                is SectionMeta -> generateSection(elem)
                is RepeatableMeta -> generateRepeatable(elem)
                else -> CodeBlock.of("") // unreachable
            }
        })
        doubleUnindent()
        add("\n)\n")
        doubleUnindent()
        add(")")
    }.build()

    private fun generateRepeatable(rep: RepeatableMeta): CodeBlock = CodeBlock.builder().apply {
        add("%T(\n", Repeatable::class)
        doubleIndent()
        add("groupId = %S,\n", rep.groupId)
        add("title = %S,\n", rep.title)
        add("sectionTemplates = listOf(\n")
        doubleIndent()
        add(rep.sectionTemplates.joinToCode(",\n") { generateSection(it) })
        doubleUnindent()
        add("\n)\n")
        doubleUnindent()
        add(")")
    }.build()

    private fun generateSection(section: SectionMeta): CodeBlock = CodeBlock.builder().apply {
        add("%T(\n", Section::class)
        doubleIndent()
        add("key = %S,\n", section.label)
        add("title = %S,\n", section.label.replaceFirstChar { it.uppercase() })
        add("description = %S,\n", section.description)
        add("fields = listOf(\n")
        doubleIndent()
        add(section.fields.joinToCode(",\n") { generateField(it) })
        doubleUnindent()
        add("\n)\n")
        doubleUnindent()
        add(")")
    }.build()

    private fun generateField(field: FieldMeta): CodeBlock = CodeBlock.builder().apply {
        add("%T(\n", Field::class)
        doubleIndent()
        add("key = %S,\n", field.key)
        add("label = %S,\n", field.label)
        add("type = %L,", field.type)
        if (!field.options.isNullOrEmpty()) {
            add("\noptions = listOf(")
            add(field.options.joinToString(", ") { "%S" }, *field.options.toTypedArray())
            add(")")
        }
        doubleUnindent()
        add("\n)")
    }.build()


    fun CodeBlock.Builder.doubleIndent() {
        indent()
        indent()
    }

    fun CodeBlock.Builder.doubleUnindent() {
        unindent()
        unindent()
    }
}
