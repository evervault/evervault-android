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

            // https://github.com/evervault/evervault-android/security/dependabot/28
            // CVE-2026-33870 — HTTP request smuggling via chunked-extension quoted string
            // CVE-2026-33871 — HTTP/2 CONTINUATION flood DoS (bundled in same release)
            force("io.netty:netty-codec-http:4.1.132.Final")

            // https://github.com/evervault/evervault-android/security/dependabot/29
            // CVE-2026-33871 — HTTP/2 CONTINUATION flood DoS
            force("io.netty:netty-codec-http2:4.1.132.Final")

            // https://github.com/evervault/evervault-android/security/dependabot/33
            // CVE-2026-3505 — bcpg-jdk18on pre-auth resource exhaustion
            force("org.bouncycastle:bcpg-jdk18on:1.84")

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
