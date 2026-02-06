import java.util.Properties

buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.13.2")
    }
}

//// Load properties from local.properties
val localProperties = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.reader()?.use { load(it) }
}

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

allprojects {
    configurations.all {
        resolutionStrategy {
            // https://github.com/evervault/evervault-android/security/dependabot/4
            force("io.netty:netty-handler:4.1.94.Final")

            // https://github.com/evervault/evervault-android/security/dependabot/22
            force("io.netty:netty-codec:4.1.125.Final")

            // https://github.com/evervault/evervault-android/security/dependabot/26
            force("io.netty:netty-codec-http:4.1.129.Final")

            // https://github.com/evervault/evervault-android/security/dependabot/3
            force("com.google.android.gms:play-services-basement:18.0.2")
        }
    }

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
