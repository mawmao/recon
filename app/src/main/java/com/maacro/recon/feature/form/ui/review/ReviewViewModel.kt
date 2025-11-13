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
import com.maacro.recon.feature.form.data.registry.FormFactory
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
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

@HiltViewModel(assistedFactory = ReviewViewModel.Factory::class)
class ReviewViewModel @AssistedInject constructor(
    private val formRepository: FormRepository,
    private val authRepository: AuthRepository,
    private val formFactory: FormFactory,
    @Assisted("formType") private val formTypeName: String,
    @Assisted("mfid") private val mfid: String,
    @ApplicationContext private val context: Context
) : ViewModel(), VMActions<ReviewAction> {

    val form = formFactory.getTemplate(FormType.fromName(formTypeName))

    private val _state = VMState(ReviewScreenState(currentForm = form))
    val state = _state.flow

    private val _events = VMEvents<ReviewEvent>()
    val events = _events.events

    override fun onAction(action: ReviewAction) {
        when (action) {
            is ReviewAction.BackClick -> toggleBackConfirm(true)
            is ReviewAction.BackDismiss -> toggleBackConfirm(false)
            is ReviewAction.ExitClick -> toggleExitConfirm(true)
            is ReviewAction.ExitDismiss -> toggleExitConfirm(false)
            is ReviewAction.FormSubmit -> submitForm(action.answers)
        }
    }

    private fun toggleBackConfirm(show: Boolean) =
        _state.update { it.copy(isBackConfirmShown = show) }

    private fun toggleExitConfirm(show: Boolean) =
        _state.update { it.copy(isExitConfirmShown = show) }

    private fun submitForm(answers: Map<String, FieldValue>) {
        viewModelScope.launch {
            val userId = getAuthenticatedUserId()
            val payloadJson = buildPayloadJson(buildSubmission(form, answers))
            val entry = FormEntryEntity(
                mfid = mfid,
                activityType = formTypeName.toActivityType(),
                collectedBy = userId ?: "",
                payloadJson = payloadJson.toString(),
            )

            try {
                val id = formRepository.saveFormEntry(entry)
                if (id > 0) {
                    enqueueSyncWork()
                    _events.sendEvent(ReviewEvent.SubmitSuccess)
                }
            } catch (e: Exception) {
                Timber.e(e, "Database insert failed")
            }
        }
    }

    private suspend fun getAuthenticatedUserId(): String? {
        return when (val status = authRepository.sessionStatus.first()) {
            is SessionStatus.Authenticated -> status.session.user?.id
            else -> throw IllegalStateException("User not authenticated")
        }
    }

    private fun buildPayloadJson(result: SubmissionResult): JSONObject = JSONObject().apply {
        when (result) {
            is SubmissionResult.Single -> result.ungrouped.forEach { (key, value) ->
                put(key, fieldValueToJson(value))
            }

            is SubmissionResult.Dynamic -> {
                result.fixedSections.forEach { (key, value) ->
                    put(key, fieldValueToJson(value))
                }

                result.dynamicSections.forEach { (groupId, instances) ->
                    val instanceArray = JSONArray(
                        instances.map { instance ->
                            JSONObject(instance.mapValues { (_, v) -> fieldValueToJson(v) })
                        }
                    )
                    put(groupId, instanceArray)
                }
            }
        }
    }

    private fun enqueueSyncWork() {
        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequestBuilder<FormSyncWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(SyncConstraints)
                .build()
        )
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