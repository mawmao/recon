package com.maacro.recon.core.database.di

import android.content.Context
import androidx.room.Room
import com.maacro.recon.core.database.ReconDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesReconDatabase(
        @ApplicationContext context: Context,
    ): ReconDatabase = Room.databaseBuilder(
        context,
        ReconDatabase::class.java,
        "recon-database",
    )
        .build()
}