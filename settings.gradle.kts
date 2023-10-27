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
include(":evervault-cages")
include(":evervault-common")
include(":evervault-common-e2e")