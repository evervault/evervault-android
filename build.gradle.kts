buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
    }
}
group "com.evervault.sdk"
plugins {
    kotlin("jvm") version "1.8.21" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

val ossrhUsername: String? by project
val ossrhPassword: String? by project

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
