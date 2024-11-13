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
include(":evervault-crypto")
include(":evervault-crypto-e2e")
include(":evervault-enclaves")
