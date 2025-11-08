package com.maacro.recon.core.common

import timber.log.Timber

inline fun <T> measureTimeMillis(blockName: String, block: () -> T): T {
    val start = System.nanoTime()
    val result = block()
    val durationMs = (System.nanoTime() - start) / 1_000_000
    Timber.d("$blockName took $durationMs ms")
    return result
}

inline fun <T> measureTimeMicro(blockName: String, block: () -> T): T {
    val start = System.nanoTime()
    val result = block()
    val durationUs = (System.nanoTime() - start) / 1_000
    Timber.d("$blockName took $durationUs μs")
    return result
}

inline fun <T> measureTimeNano(blockName: String, block: () -> T): T {
    val start = System.nanoTime()
    val result = block()
    val durationNs = System.nanoTime() - start
    Timber.d("$blockName took $durationNs ns")
    return result
}
