package com.maacro.recon.feature.form.di

import com.maacro.recon.core.database.dao.FormEntryDao
import com.maacro.recon.feature.form.data.FormRepository
import com.maacro.recon.feature.form.data.FormRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FormModule {

    @Provides
    @Singleton
    fun providesFormRepository(
        formEntryDao: FormEntryDao
    ): FormRepository = FormRepositoryImpl(formEntryDao = formEntryDao)
}