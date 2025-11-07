package com.maacro.recon.feature.auth.di

import com.maacro.recon.feature.auth.data.AuthRepository
import com.maacro.recon.feature.auth.data.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun providesAuthRepository(supabaseClient: SupabaseClient): AuthRepository =
        AuthRepositoryImpl(supabaseClient = supabaseClient)
}
