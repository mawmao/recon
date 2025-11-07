package com.maacro.recon.core.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.maacro.recon.core.common.dispatcher.Dispatcher
import com.maacro.recon.core.common.dispatcher.ReconDispatchers
import com.maacro.recon.core.network.service.SupabaseService
import com.maacro.recon.feature.form.data.FormRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
internal class FormSyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(ReconDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val formRepository: FormRepository,
    private val supabaseService: SupabaseService
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        return@withContext try {
            val pending = formRepository.getPendingSyncOnce()

            pending.forEach { entry ->
                Log.d(
                    "recon:form-sync-worker",
                    "Pending entry: id=${entry.id}, mfid=${entry.mfid}, synced=${entry.synced}"
                )
            }

            if (pending.isEmpty()) {
                Log.d("recon:form-sync-worker", "No pending forms to sync")
                return@withContext Result.success()
            }

            var hasError = false
            for (entry in pending) {
                try {
                    supabaseService.uploadForm(entry)
                    formRepository.markAsSynced(entry.id)
                    Log.d("recon:form-sync-worker", "Synced entry id=${entry.id}")
                } catch (e: Exception) {
                    hasError = true
                    Log.e("recon:form-sync-worker", "Error syncing entry id=${entry.id}", e)
                }
            }

            if (hasError) {
                Log.d("recon:form-sync-worker", "Partial failure detected")
                Result.retry()
            } else {
                Log.d("recon:form-sync-worker", "All entries synced successfully")
                Result.success()
            }

        } catch (e: Exception) {
            Log.e("recon:form-sync-worker", "Fatal sync error", e)
            Result.failure()
        }
    }

    companion object {
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<FormSyncWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .build()
    }
}
