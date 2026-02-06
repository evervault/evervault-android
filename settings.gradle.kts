pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
        maven("https://jitpack.io")
    }
    plugins {
        id("com.android.application") version "8.13.2" apply false
        id("com.android.library") version "8.13.2" apply false
        id("org.jetbrains.kotlin.android") version "2.2.21" apply false
        id("org.jetbrains.kotlin.plugin.serialization") version "2.2.21" apply false
        id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false
    }
}

rootProject.name = "evervault-android"
include(":sampleapplication")
include(":evervault-inputs")
include(":evervault-cages")
include(":evervault-enclaves")
include(":evervault-core")
