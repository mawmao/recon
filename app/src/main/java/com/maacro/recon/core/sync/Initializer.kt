package com.maacro.recon.core.sync

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager

object Sync {
    fun initialize(context: Context) {
        Log.d("recon:sync", "Initializing sync")
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SYNC_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                FormSyncWorker.startUpSyncWork(),
            )
        }
    }
}

internal const val SYNC_WORK_NAME = "SyncWorkName"
