pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "evervault-android"
include(":sampleapplication")
include(":evervault-inputs")
include(":evervault-common")
include(":evervault-enclaves")