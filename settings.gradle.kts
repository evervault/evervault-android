pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.8.22"
        id("com.android.library") version "8.0.2"
        id("org.jetbrains.kotlin.android") version "1.8.20"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":sampleapplication")

rootProject.name = "evervault-android"
include("evervault-core")
include(":sampleapplication")
include(":evervault-inputs")
