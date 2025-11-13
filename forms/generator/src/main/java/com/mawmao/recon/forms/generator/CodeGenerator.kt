package com.mawmao.recon.forms.generator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.mawmao.recon.forms.model.Field;
import com.mawmao.recon.forms.model.FieldOptions
import com.mawmao.recon.forms.model.Form
import com.mawmao.recon.forms.model.Repeatable
import com.mawmao.recon.forms.model.Section
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock;
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.joinToCode
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.writeTo

private fun KSClassDeclaration.toClassName(): ClassName =
    ClassName(this.packageName.asString(), this.simpleName.asString())

private fun FormMeta.toFormType(): String = name
    .replace(Regex("([a-z])([A-Z])"), "$1_$2")
    .uppercase()

object HiltAnnotations {
    val EntryPoint = AnnotationSpec.builder(ClassName("dagger.hilt", "EntryPoint")).build()
    val InstallInSingleton = AnnotationSpec
        .builder(ClassName("dagger.hilt", "InstallIn"))
        .addMember("%T::class", ClassName("dagger.hilt.components", "SingletonComponent"))
        .build()
}


fun TypeSpec.Builder.addInjectedConstructor(
    classes: List<Pair<String, String>>, files: List<KSFile>? = null
): TypeSpec.Builder {
    val constructorBuilder = FunSpec.constructorBuilder()
        .addAnnotation(ClassName("javax.inject", "Inject"))

    classes.forEach { (fqcn, paramName) ->
        val clazz = ClassName.bestGuess(fqcn)
        constructorBuilder.addParameter(paramName, clazz)

        addProperty(
            PropertySpec.builder(paramName, clazz)
                .initializer(paramName)
                .addModifiers(KModifier.PRIVATE)
                .build()
        )
    }

    files?.forEach { file -> addOriginatingKSFile(file) }

    return this.primaryConstructor(constructorBuilder.build())
}



object FormCodeGenerator {

    fun generateFormClassFile(
        codeGenerator: CodeGenerator,
        metadata: FormMeta,
        file: KSFile?,
    ) {
        if (file == null) return

        fun TypeSpec.Builder.addFormTemplate(formMeta: FormMeta): TypeSpec.Builder =
            this.addFunction(
                FunSpec.builder("getTemplate")
                    .returns(Form::class)
                    .addCode(generateFormTemplate(formMeta))
                    .build()
            )

        fun TypeSpec.Builder.addFormClassInjectedConstructor(providers: List<String>): TypeSpec.Builder {
            val classes = providers.map { fqcn ->
                val paramName = fqcn.substringAfterLast(".").replaceFirstChar { it.lowercase() }
                fqcn to paramName
            }
            return addInjectedConstructor(classes)
        }

        fun FileSpec.Builder.addFormClass(): FileSpec.Builder = addType(
            TypeSpec.classBuilder("${metadata.name}Form")
                .addFormClassInjectedConstructor(metadata.providers)
                .addFormTemplate(metadata)
                .addOriginatingKSFile(file)
                .build()
        )

        FileSpec.builder(metadata.packageName, "${metadata.name}Form")
            .addFormClass()
            .build()
            .writeTo(codeGenerator, aggregating = false)
    }

    fun generateFormEntryPoint(
        codeGenerator: CodeGenerator,
        packageName: String,
        file: KSFile?
    ) {
        if (packageName.isEmpty() || file == null) return

        fun FileSpec.Builder.addFormEntryPoint(): FileSpec.Builder = addType(
            typeSpec = TypeSpec.interfaceBuilder("FormFactoryEntryPoint")
                .addAnnotation(HiltAnnotations.EntryPoint)
                .addAnnotation(HiltAnnotations.InstallInSingleton)
                .addFunction(
                    FunSpec.builder("formFactory")
                        .addModifiers(KModifier.ABSTRACT)
                        .returns(ClassName(packageName, "FormFactory"))
                        .build()
                )
                .addOriginatingKSFile(file)
                .build()
        )

        FileSpec.builder(packageName, "FormFactoryEntryPoint")
            .addFormEntryPoint()
            .build()
            .writeTo(codeGenerator, aggregating = false)
    }

    fun generateFormFactory(
        codeGenerator: CodeGenerator,
        forms: List<FormMeta>,
        ftClass: KSClassDeclaration,
        packageName: String,
        file: KSFile?
    ) {
        if (forms.isEmpty()) return
        if (file == null) return

        fun FunSpec.Builder.addExhaustiveWhen(
            forms: List<FormMeta>,
            formTypeClassName: ClassName
        ): FunSpec.Builder {
            val whenCode = CodeBlock.builder().beginControlFlow("return when(type)")

            forms.forEach { form ->
                val paramName = form.name.replaceFirstChar { it.lowercase() } + "Form"
                val enumEntry = form.toFormType()
                whenCode.addStatement(
                    "%T.%L -> %L.getTemplate()",
                    formTypeClassName,
                    enumEntry,
                    paramName
                )
            }

            whenCode.endControlFlow()
            return addCode(whenCode.build())
        }

        fun TypeSpec.Builder.addGetTemplateMethod(
            forms: List<FormMeta>,
            ftClass: KSClassDeclaration
        ): TypeSpec.Builder {
            val className = ftClass.toClassName()
            val methodBuilder = FunSpec.builder("getTemplate")
                .addParameter("type", className)
                .returns(Form::class)
                .addExhaustiveWhen(forms, className)

            return addFunction(methodBuilder.build())
        }

        fun TypeSpec.Builder.addFormFactoryInjectedConstructor(forms: List<FormMeta>): TypeSpec.Builder {
            val classes = forms.map { form ->
                val className = "${form.packageName}.${form.name}Form"
                val paramName = form.name.replaceFirstChar { it.lowercase() } + "Form"
                className to paramName
            }
            val files = forms.mapNotNull { it.containingFile }
            return addInjectedConstructor(classes, files)
        }

        fun FileSpec.Builder.addFormFactory(): FileSpec.Builder = addType(
            typeSpec = TypeSpec
                .classBuilder("FormFactory")
                .addFormFactoryInjectedConstructor(forms)
                .addGetTemplateMethod(forms, ftClass)
                .build()
        )

        FileSpec.builder(packageName, "FormFactory")
            .addFormFactory()
            .build()
            .writeTo(codeGenerator, aggregating = true)
    }

    private fun generateFormTemplate(model: FormMeta): CodeBlock = CodeBlock.builder().apply {
        add("return %T(\n", Form::class)
        doubleIndent()
        add("label = %S,\n", model.label)
        add("elements = listOf(\n")
        doubleIndent()
        add(model.elements.joinToCode(",\n") { elem ->
            when (elem) {
                is SectionMeta -> generateSection(elem)
                is RepeatableMeta -> generateRepeatable(elem)
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

        add("templateSections = listOf(\n")
        doubleIndent()
        add(rep.templateSections.joinToCode(",\n") { generateSection(it) })
        doubleUnindent()
        add("\n),\n")

        add("sections = listOf(\n")
        doubleIndent()
        add(rep.sections.joinToCode(",\n") { generateSection(it) })
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
        if (section.groupId.isNotEmpty()) add("groupId = %S,\n", section.groupId)
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

        // static options
        field.options?.takeIf { it.isNotEmpty() }?.let { opts ->
            add("\noptions = %T.Static(listOf(", FieldOptions::class)
            add(opts.joinToString(", ") { "%S" }, *opts.toTypedArray())
            add(")),")
        }

        field.providerMeta?.let { meta ->
            val providerVar =
                meta.providerFqcn.substringAfterLast(".").replaceFirstChar { it.lowercase() }
            add(
                "\noptions = %T.Dynamic(provider = { %L ->\n",
                FieldOptions::class,
                if (meta.takesParent) "parentValue" else "_"
            )
            if (meta.takesParent) {
                add("    %L.%L(parentValue)\n", providerVar, meta.methodName)
            } else {
                add("    %L.%L()\n", providerVar, meta.methodName)
            }
            add("}),")
        }

        if (field.dependsOn.isNotEmpty()) add("\ndependsOn = %S,", field.dependsOn)
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
