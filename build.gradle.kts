buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.11.1")
    }
}

// Load properties from local.properties
val localProperties = java.util.Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(java.io.FileInputStream(localPropertiesFile))
}

group "com.evervault.sdk"
plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

val ossrhUsername: String? = localProperties.getProperty("ossrhUsername")
val ossrhPassword: String? = localProperties.getProperty("ossrhPassword")

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/content/repositories/snapshots/"))
            stagingProfileId.set("7050c947df3733")
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}
