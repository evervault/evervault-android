pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
//}

include(":sampleapplication")

rootProject.name = "evervault-android"
include("evervault-core")
include(":sampleapplication")
include(":evervault-inputs")
