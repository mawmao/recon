package com.maacro.recon.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maacro.recon.core.database.dao.BarangayDao
import com.maacro.recon.core.database.dao.CityMunicipalityDao
import com.maacro.recon.core.database.dao.FormEntryDao
import com.maacro.recon.core.database.dao.ProvinceDao
import com.maacro.recon.core.database.model.BarangayEntity
import com.maacro.recon.core.database.model.CityMunicipalityEntity
import com.maacro.recon.core.database.model.FormEntryEntity
import com.maacro.recon.core.database.model.ProvinceEntity

@Database(
    entities = [
        FormEntryEntity::class,
        ProvinceEntity::class,
        CityMunicipalityEntity::class,
        BarangayEntity::class
    ],
    version = 1,
    exportSchema = true,
)
abstract class ReconDatabase : RoomDatabase() {
    abstract fun formEntryDao(): FormEntryDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun cityMunicipalityDao(): CityMunicipalityDao
    abstract fun barangayDao(): BarangayDao
}
