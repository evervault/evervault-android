package com.evervault.sdk.test

import com.evervault.sdk.BuildConfig

private fun getenv(key: String): String? = System.getenv(key)

fun getAppUUID(): String {
    // BuildConfig will be empty in release builds.
    return getenv("EV_APP_UUID") ?: BuildConfig.EV_APP_UUID
}

fun getTeamUUID(): String {
    // BuildConfig will be empty in release builds.
    return getenv("EV_TEAM_UUID") ?: BuildConfig.EV_TEAM_UUID
}

fun getAPIKey(): String {
    // BuildConfig will be empty in release builds
    return getenv("EV_API_KEY") ?: BuildConfig.EV_API_KEY
}
