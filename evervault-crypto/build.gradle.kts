import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
    kotlin("plugin.serialization") version "1.8.21"
}

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

android {
    namespace = "com.evervault.sdk.crypto"
    compileSdk = 33
    val prop = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "evervault-crypto/version.properties")))
    }
    version = prop.getProperty("VERSION_NAME")
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

val ktorVersion = "2.3.1"

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    testImplementation("junit:junit:4.13.2")
    implementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.evervault.sdk"
            artifactId = "evervault-crypto"
            version = version

            afterEvaluate {
                from(components["release"])
            }
        }
    }
    publications.withType<MavenPublication> {
        pom {
            name.set("Evervault")
            description.set("Evervault Android SDK")
            url.set("https://github.com/evervault/evervault-android")
            developers {
                developer {
                    name.set("engineering")
                    organization.set("Evervault")
                    email.set("engineering@evervault.com")
                }
            }
            scm {
                connection.set("scm:git:ssh://git@github.com:evervault/evervault-android.git")
                url.set("https://github.com/evervault/evervault-android")
            }
            licenses {
                license {
                    name.set("The MIT License (MIT)")
                    url.set("https://mit-license.org")
                }
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey ?: "", signingPassword ?: "")

    sign(publishing.publications["release"])
}
