package com.maacro.recon.feature.auth.data

import io.github.jan.supabase.auth.exception.AuthRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException

fun mapSignInErrors(error: Throwable): String {
    return when (error) {
        is AuthRestException -> "Invalid email or password."
        is HttpRequestTimeoutException -> "Network timed out. Try again."
        is HttpRequestException -> "Check your internet connection."
        else -> "Something went wrong. Please try again."
    }
}


fun mapSignOutErrors(error: Throwable): String {
    return when (error) {
        is AuthRestException -> "Authentication failed. Please check your credentials or try again."
        is HttpRequestTimeoutException -> "Network timed out. Please try again."
        is HttpRequestException -> "Unable to connect. Check your internet connection."
        else -> "Something went wrong. Please try again."
    }
}
