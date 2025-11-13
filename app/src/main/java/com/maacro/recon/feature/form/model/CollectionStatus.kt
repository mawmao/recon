package com.maacro.recon.feature.form.model

import android.util.Log as AndroidLog
import timber.log.Timber

/**
 * Purely for logging
 */

private const val PREFIX = "DC_STATUS"

sealed class CollectionStatus {
    sealed class Phase(private val phaseName: String) : CollectionStatus() {
        fun Start(extra: String? = null) =
            "${phaseName}_STARTED${extra?.let { " ($it)" } ?: ""}"

        fun End(extra: String? = null) =
            "${phaseName}_ENDED${extra?.let { " ($it)" } ?: ""}"

        fun Cancel(extra: String? = null) =
            "${phaseName}_CANCELLED${extra?.let { " ($it)" } ?: ""}"

        fun Success(extra: String? = null) =
            "${phaseName}_SUCCESS${extra?.let { " ($it)" } ?: ""}"
    }

    object Collect : Phase("COLLECT")
    object Scan : Phase("SCAN")
    object Confirm : Phase("CONFIRM")
    object Question : Phase("QUESTION")
    object Review : Phase("REVIEW")

    object Log {
        fun i(vararg messages: String) = logCollection(*messages, priority = AndroidLog.INFO)
        fun d(vararg messages: String) = logCollection(*messages, priority = AndroidLog.DEBUG)
        fun e(vararg messages: String) = logCollection(*messages, priority = AndroidLog.ERROR)
        fun v(vararg messages: String) = logCollection(*messages, priority = AndroidLog.VERBOSE)

        private fun logCollection(vararg messages: String, priority: Int = AndroidLog.INFO) {
            if (messages.isEmpty()) return

            fun Int.log(logMessage: String) = when (this) {
                AndroidLog.INFO -> Timber.i("$PREFIX: $logMessage")
                AndroidLog.DEBUG -> Timber.d("$PREFIX: $logMessage")
                AndroidLog.ERROR -> Timber.e("$PREFIX: $logMessage")
                AndroidLog.VERBOSE -> Timber.v("$PREFIX: $logMessage")
                else -> Timber.v("$PREFIX: $logMessage")
            }

            if (messages.size == 1) {
                priority.log(messages[0])
            } else {
                val logMessage = messages.joinToString(" -> ") { it }
                priority.log(logMessage)
            }
        }
    }
}
