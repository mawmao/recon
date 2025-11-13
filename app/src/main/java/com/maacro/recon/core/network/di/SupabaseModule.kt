package com.maacro.recon.core.network.di

import com.maacro.recon.BuildConfig
import com.maacro.recon.core.network.service.SupabaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.logging.LogLevel
import io.github.jan.supabase.postgrest.Postgrest
import jakarta.inject.Singleton
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun providesSupabaseClient(): SupabaseClient {
        Timber.d("Using Supabase URL: ${BuildConfig.SUPABASE_URL}")
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            defaultLogLevel = LogLevel.DEBUG
            install(Postgrest)
            install(Auth)
        }
    }

    @Provides
    @Singleton
    fun providesSupabaseService(client: SupabaseClient): SupabaseService =
        SupabaseService(client = client)

}