package com.maacro.recon.feature.form.ui.review

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.domain.VMActions
import com.maacro.recon.core.domain.VMEvents
import com.maacro.recon.core.domain.VMState
import com.maacro.recon.core.sync.SyncConstraints
import com.maacro.recon.core.sync.FormSyncWorker
import com.maacro.recon.feature.auth.data.AuthRepository
import com.maacro.recon.feature.form.data.FormRepository
import com.maacro.recon.feature.form.data.registry.util.toActivityType
import com.maacro.recon.feature.form.model.FieldValue
import com.maacro.recon.feature.form.model.FormType
import com.maacro.recon.feature.form.ui.question.FormAnswers
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Map.entry

@HiltViewModel(assistedFactory = ReviewViewModel.Factory::class)
class ReviewViewModel @AssistedInject constructor(
    private val formRepository: FormRepository,
    private val authRepository: AuthRepository,
    @Assisted("formType") val formTypeName: String,
    @Assisted("mfid") val mfid: String,
    @ApplicationContext private val context: Context
) : ViewModel(), VMActions<ReviewAction> {

    val currentForm = FormType.fromName(formTypeName).template

    private val _state = VMState(initialState = ReviewScreenState(currentForm = currentForm))
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

                    val result = action.answers.mapValues { (_, v) ->
                        when (v) {
                            is FieldValue.Text -> v.value
                            is FieldValue.Number -> v.value
                            is FieldValue.Date -> v.value
                            is FieldValue.Dropdown -> v.selected
                            is FieldValue.Checkbox -> v.checked
                        }
                    }
                    val payloadJson = JSONObject(result).toString()

                    val entry = FormEntryEntity(
                        mfid = mfid,
                        activityType = formTypeName.toActivityType(),
                        collectedBy = currentUserId ?: "",
                        payloadJson = payloadJson,
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
                        Log.e("recon:review-vm", "Database insert failed", e)
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