package com.maacro.recon.core.database.di

import com.maacro.recon.core.database.ReconDatabase
import com.maacro.recon.core.database.dao.BarangayDao
import com.maacro.recon.core.database.dao.CityMunicipalityDao
import com.maacro.recon.core.database.dao.FormEntryDao
import com.maacro.recon.core.database.dao.ProvinceDao
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

    @Provides
    fun providesProvinceDao(
        database: ReconDatabase,
    ): ProvinceDao = database.provinceDao()

    @Provides
    fun providesCityMunicipalityDao(
        database: ReconDatabase,
    ): CityMunicipalityDao = database.cityMunicipalityDao()

    @Provides
    fun providesBarangayDao(
        database: ReconDatabase,
    ): BarangayDao = database.barangayDao()
}
