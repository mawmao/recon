package com.maacro.recon.feature.form.ui.review

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMEvents
import com.maacro.recon.core.domain.VMState
import com.maacro.recon.core.sync.FormSyncWorker
import com.maacro.recon.core.sync.SyncConstraints
import com.maacro.recon.feature.auth.data.AuthRepository
import com.maacro.recon.feature.form.data.FormRepository
import com.maacro.recon.feature.form.data.registry.util.toActivityType
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.model.FormType
import com.maacro.recon.feature.form.model.SubmissionResult
import com.maacro.recon.feature.form.model.buildSubmission
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import kotlin.apply

@HiltViewModel(assistedFactory = ReviewViewModel.Factory::class)
class ReviewViewModel @AssistedInject constructor(
    private val formRepository: FormRepository,
    private val authRepository: AuthRepository,
    @Assisted("formType") val formTypeName: String,
    @Assisted("mfid") val mfid: String,
    @ApplicationContext private val context: Context
) : ViewModel(), VMActions<ReviewAction> {

    val form = FormType.fromName(formTypeName).template

    private val _state = VMState(initialState = ReviewScreenState(currentForm = form))
    val state = _state.flow

    private val _events = VMEvents<ReviewEvent>()
    val events = _events.events

    override fun onAction(action: ReviewAction) {
        when (action) {
            is ReviewAction.BackClick -> _state.update { it.copy(isBackConfirmShown = true) }
            is ReviewAction.BackDismiss -> _state.update { it.copy(isBackConfirmShown = false) }
            is ReviewAction.ExitClick -> _state.update { it.copy(isExitConfirmShown = true) }
            is ReviewAction.ExitDismiss -> _state.update { it.copy(isExitConfirmShown = false) }
            is ReviewAction.FormSubmit -> {
                viewModelScope.launch {
                    val currentUserId = when (val status = authRepository.sessionStatus.first()) {
                        is SessionStatus.Authenticated -> status.session.user?.id
                        else -> throw IllegalStateException("User not authenticated")
                    }

                    val payloadJson = JSONObject().apply {
                        when (val submission = buildSubmission(form, action.answers)) {
                            is SubmissionResult.Dynamic -> {
                                submission.fixedSections.forEach { (key, value) ->
                                    put(key, fieldValueToJson(value))
                                }

                                submission.dynamicSections.forEach { (groupId, instances) ->
                                    val instanceArray = JSONArray()
                                    instances.forEach { instance ->
                                        val instanceObj = JSONObject()
                                        instance.forEach { (key, value) ->
                                            instanceObj.put(key, fieldValueToJson(value))
                                        }
                                        instanceArray.put(instanceObj)
                                    }
                                    put(groupId, instanceArray)
                                }
                            }

                            is SubmissionResult.Single -> {
                                submission.ungrouped.forEach { (key, value) ->
                                    put(key, fieldValueToJson(value))
                                }
                            }
                        }
                    }

                    Timber.d("Final `payloadJson`: \n${payloadJson}")
                    val entry = FormEntryEntity(
                        mfid = mfid,
                        activityType = formTypeName.toActivityType(),
                        collectedBy = currentUserId ?: "",
                        payloadJson = payloadJson.toString(),
                    )

                    try {
                        val id = formRepository.saveFormEntry(entry)
                        if (id > 0) {
                            WorkManager.getInstance(context).enqueue(
                                OneTimeWorkRequestBuilder<FormSyncWorker>()
                                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                                    .setConstraints(SyncConstraints)
                                    .build()
                            )
                            _events.sendEvent(ReviewEvent.SubmitSuccess)
                        }
                    } catch (e: Exception) {
                        Timber.e("Database insert failed $e")
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("formType") type: String,
            @Assisted("mfid") mfid: String
        ): ReviewViewModel
    }
}

private fun fieldValueToJson(value: FieldValue): Any = when (value) {
    is FieldValue.Text -> value.value
    is FieldValue.Number -> value.value
    is FieldValue.Date -> value.value
    is FieldValue.Dropdown -> value.selected
    is FieldValue.Checkbox -> value.checked
}

data class ReviewScreenState(
    val currentForm: com.mawmao.recon.forms.model.Form,
    val isBackConfirmShown: Boolean = false,
    val isExitConfirmShown: Boolean = false,
)

sealed class ReviewEvent {
    object SubmitSuccess : ReviewEvent()
}

sealed class ReviewAction {
    object ExitClick : ReviewAction()
    object ExitDismiss : ReviewAction()
    object BackClick : ReviewAction()
    object BackDismiss : ReviewAction()
    data class FormSubmit(val answers: Map<String, FieldValue>) : ReviewAction()
}