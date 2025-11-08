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
import timber.log.Timber

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
                Timber.d("Pending entry: id=${entry.id}, mfid=${entry.mfid}, synced=${entry.synced}")
            }

            if (pending.isEmpty()) {
                Timber.d("No pending forms to sync")
                return@withContext Result.success()
            }

            var hasError = false
            for (entry in pending) {
                try {
                    supabaseService.uploadForm(entry)
                    formRepository.markAsSynced(entry.id)
                    Timber.d("Synced entry id=${entry.id}")
                } catch (e: Exception) {
                    hasError = true
                    Timber.e(e, "Error syncing entry id=${entry.id}")
                }
            }

            if (hasError) {
                Timber.w("Partial failure detected")
                Result.retry()
            } else {
                Timber.d("All entries synced successfully")
                Result.success()
            }

        } catch (e: Exception) {
            Timber.e(e, "Fatal sync error")
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
