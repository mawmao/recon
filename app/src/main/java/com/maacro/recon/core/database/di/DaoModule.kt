package com.maacro.recon.core.database.di

import com.maacro.recon.core.database.ReconDatabase
import com.maacro.recon.core.database.dao.FormEntryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun providesFormEntryDao(
        database: ReconDatabase,
    ): FormEntryDao = database.formEntryDao()
}