package com.maacro.recon.feature.form.data

import com.maacro.recon.core.database.dao.FormEntryDao
import com.maacro.recon.core.database.model.FormEntryEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface FormRepository {
    fun getAllEntries(): Flow<List<FormEntryEntity>>

    suspend fun saveFormEntry(entry: FormEntryEntity): Long
    suspend fun getPendingSyncOnce(): List<FormEntryEntity>

    suspend fun markAsSynced(id: Int): Unit

    // NOTE: development only
    suspend fun clearDatabase()
}

class FormRepositoryImpl @Inject constructor(
    private val formEntryDao: FormEntryDao
) : FormRepository {

    override fun getAllEntries(): Flow<List<FormEntryEntity>> = formEntryDao.getAll()

    override suspend fun saveFormEntry(entry: FormEntryEntity): Long = formEntryDao.insert(entry)

    override suspend fun getPendingSyncOnce(): List<FormEntryEntity> = formEntryDao.getPendingSyncOnce()

    override suspend fun markAsSynced(id: Int) = formEntryDao.markAsSynced(id)

    // NOTE: development only
    override suspend fun clearDatabase() = formEntryDao.clearAll()
}