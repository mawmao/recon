package com.maacro.recon.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maacro.recon.core.database.dao.FormEntryDao
import com.maacro.recon.core.database.model.FormEntryEntity

@Database(
    entities = [
        FormEntryEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class ReconDatabase : RoomDatabase() {
    abstract fun formEntryDao(): FormEntryDao
}
