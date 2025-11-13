package com.maacro.recon.feature.form.di

import android.content.Context
import com.maacro.recon.core.database.dao.BarangayDao
import com.maacro.recon.core.database.dao.CityMunicipalityDao
import com.maacro.recon.core.database.dao.FormEntryDao
import com.maacro.recon.core.database.dao.ProvinceDao
import com.maacro.recon.feature.form.data.CoordinatesRepository
import com.maacro.recon.feature.form.data.CoordinatesRepositoryImpl
import com.maacro.recon.feature.form.data.CoordinatesService
import com.maacro.recon.feature.form.data.CoordinatesServiceImpl
import com.maacro.recon.feature.form.data.FormRepository
import com.maacro.recon.feature.form.data.FormRepositoryImpl
import com.maacro.recon.feature.form.data.LocationRepository
import com.maacro.recon.feature.form.data.LocationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FormModule {

    @Provides
    @Singleton
    fun provideCoordinatesService(
        @ApplicationContext context: Context
    ): CoordinatesService = CoordinatesServiceImpl(context)

    @Provides
    @Singleton
    fun provideCoordinatesRepository(
        coordinatesService: CoordinatesService,
    ): CoordinatesRepository = CoordinatesRepositoryImpl(coordinatesService)

    @Provides
    @Singleton
    fun providesFormRepository(
        formEntryDao: FormEntryDao
    ): FormRepository = FormRepositoryImpl(formEntryDao = formEntryDao)

    @Provides
    @Singleton
    fun providesLocationRepository(
        provinceDao: ProvinceDao,
        cityMunicipalityDao: CityMunicipalityDao,
        barangayDao: BarangayDao
    ): LocationRepository = LocationRepositoryImpl(
        provinceDao = provinceDao,
        cityMunicipalityDao = cityMunicipalityDao,
        barangayDao = barangayDao,
    )
}