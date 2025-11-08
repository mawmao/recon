package com.maacro.recon.feature.form.data

// - Different from `FormSectionState`
// - This holds the current mfid, formType, and answers for the current flow
// - Purposely put outside of view models since the app needs it only in-memory
// and in the scope of the current flow (scan -> confirm -> question -> review)
//


//@Composable
//fun rememberFormState(): FormState {
//    return rememberSaveable(saver = FormStateSaver) { FormState() }
//}

//private val json = Json { encodeDefaults = true }
//
//val FormStateSaver: Saver<FormState, String> = Saver(
//    save = { state ->
//        if (!state.isMfidSet || !state.isFormTypeSet) return@Saver null
//        val payload = FormStateData(
//            mfid = state.mfid!!,
//            formType = state.formType!!,
//            answers = state.answers
//        )
//        json.encodeToString(payload)
//    },
//    restore = { string ->
//        val data = json.decodeFromString<FormStateData>(string)
//        FormState().apply {
//            updateMfid(data.mfid)
//            updateFormType(data.formType)
//            data.answers.forEach { (k, v) -> updateAnswer(k, v) }
//        }
//    }
//)

//@Serializable
//private data class FormStateData(
//    val mfid: String,
//    val formType: String,
//    val answers: Map<String, FieldValue>
//)
//
//@Stable
//class FormState {
//    var mfid: String? = null
//        private set
//
//    var formType: String? = null
//        private set
//
//    private val _answers = mutableStateMapOf<String, FieldValue>()
//    val answers: Map<String, FieldValue> get() = _answers
//
//    val isMfidSet: Boolean get() = mfid != null
//    val isFormTypeSet: Boolean get() = formType != null
//
//    val mfidOrThrow: String
//        get() = mfid ?: error("mfid not set yet")
//
//    val formTypeOrThrow: String
//        get() = formType ?: error("formType not set yet")
//
//    fun updateMfid(newMfid: String) {
//        mfid = newMfid
//    }
//
//    fun updateFormType(newFormType: String) {
//        formType = newFormType
//    }
//
//    fun updateAnswer(key: String, value: FieldValue) {
//        ensureReady()
//        _answers[key] = value
//    }
//
////    fun clear() {
////        mfid = null
////        formType = null
////        _answers.clear()
////    }
//
//    private fun ensureReady() {
//        check(isMfidSet) { "mfid not set before updating answers" }
//        check(isFormTypeSet) { "formType not set before updating answers" }
//    }
//}
