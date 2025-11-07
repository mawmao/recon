package com.maacro.recon

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.maacro.recon.core.sync.Sync
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class ReconApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Sync.initialize(context = this)

        // put in debug only on production
        Timber.plant(ReconDebugTree("recon"));
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}


class ReconDebugTree(private val prefix: String) : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        val className = element.className.substringAfterLast('.').substringBefore('$')
        return "$prefix:$className:${element.lineNumber}"
    }
}
