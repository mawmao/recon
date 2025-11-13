package com.maacro.recon.feature.form.ui.question.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maacro.recon.feature.form.model.FieldValue
import com.mawmao.recon.forms.model.Field
import com.mawmao.recon.forms.model.FieldOptions
import com.mawmao.recon.forms.model.Section
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber



@Composable
fun QuestionPage(
    section: Section,
    fieldValues: Map<String, FieldValue>,
    onValueChange: (String, FieldValue) -> Unit,
) {
    val optionsCache = remember { mutableStateMapOf<String, List<String>>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(section) {
        section.fields.forEach { f ->
            when (val opts = f.options) {
                is FieldOptions.Static -> optionsCache[f.key] = opts.options
                else -> Unit
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun loadOptionsFor(field: Field, parentValue: String?): List<String> {
        val provider = (field.options as? FieldOptions.Dynamic<*>)?.provider ?: return emptyList()
        return withContext(Dispatchers.IO) {
            try {
                provider(parentValue ?: "") as? List<String> ?: emptyList()
            } catch (t: Throwable) {
                Timber.e(t, "error loading options for ${field.key}")
                emptyList()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(section.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            section.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        section.fields.forEach { field ->
            val value = fieldValues[field.key]
            val parentKey = field.dependsOn
            val parentValue = parentKey.let { (fieldValues[it] as? FieldValue.Dropdown)?.selected }
            val isVisible = parentKey.isBlank() ||
                    ((fieldValues[parentKey] as? FieldValue.Dropdown)?.selected?.isNotBlank() == true)

            LaunchedEffect(field.key, parentValue, isVisible) {
                when (field.options) {
                    is FieldOptions.Dynamic<*> -> {
                        if (isVisible) {
                            optionsCache[field.key] = loadOptionsFor(field, parentValue)
                        } else {
                            optionsCache.remove(field.key)
                        }
                    }
                    is FieldOptions.Static -> {
                        optionsCache[field.key] = (field.options as FieldOptions.Static).options
                    }
                    else -> optionsCache[field.key] = emptyList()
                }
            }

            val currentOptions = optionsCache[field.key] ?: emptyList()

            AnimatedVisibility(visible = isVisible) {
                Column {
                    QuestionField(
                        field = field,
                        value = value,
                        options = currentOptions,
                        onValueChange = { newValue ->
                            onValueChange(field.key, newValue)
                            coroutineScope.launch {
                                section.fields.filter { it.dependsOn == field.key }.forEach { dependent ->
                                    onValueChange(dependent.key, FieldValue.Dropdown(""))
                                    when (dependent.options) {
                                        is FieldOptions.Dynamic<*> -> {
                                            val newParentValue = (newValue as? FieldValue.Dropdown)?.selected
                                            optionsCache[dependent.key] = loadOptionsFor(dependent, newParentValue)
                                        }

                                        is FieldOptions.Static -> {
                                            optionsCache[dependent.key] = (dependent.options as FieldOptions.Static).options
                                        }

                                        else -> {
                                            optionsCache.remove(dependent.key)
                                        }
                                    }

                                    section.fields.filter { it.dependsOn == dependent.key }
                                        .forEach { grandchild ->
                                            onValueChange(grandchild.key, FieldValue.Dropdown(""))
                                            optionsCache.remove(grandchild.key)
                                        }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}
