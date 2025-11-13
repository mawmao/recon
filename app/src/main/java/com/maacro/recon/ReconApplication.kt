package com.maacro.recon

import android.app.Application
import android.util.Log
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
        return "$prefix:$className"
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val prefixMessage = when (priority) {
            Log.DEBUG -> "[DEBUG] $message"
            Log.INFO -> "[INFO] $message"
            Log.WARN -> "[WARN] $message"
            Log.ERROR -> "[ERROR] $message"
            Log.VERBOSE -> "[VERBOSE] $message"
            Log.ASSERT -> "[WTF] $message"
            else -> message
        }

        super.log(priority, tag, prefixMessage, t)
    }
}
