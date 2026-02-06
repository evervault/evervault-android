import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
    id("signing")
}
val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(localFile))
    }
}

val evApiKey: String = localProperties.getProperty("EV_API_KEY") ?: ""
val evAppId: String = localProperties.getProperty("EV_APP_UUID") ?: ""
val evTeamId: String = localProperties.getProperty("EV_TEAM_UUID") ?: ""

android {
    group = "com.evervault.sdk.core"
    namespace = "com.evervault.sdk"
    compileSdk = 36
    val prop = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "version.properties")))
    }
    version = prop.getProperty("VERSION_NAME")
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Default empty values
        buildConfigField("String", "EV_API_KEY", "\"\"")
        buildConfigField("String", "EV_TEAM_UUID", "\"\"")
        buildConfigField("String", "EV_APP_UUID", "\"\"")
    }

    lint {
        targetSdk = 36
    }

    buildTypes {
        debug {
            // Override with local.properties values for debug builds
            buildConfigField("String", "EV_API_KEY", "\"$evApiKey\"")
            buildConfigField("String", "EV_TEAM_UUID", "\"$evTeamId\"")
            buildConfigField("String", "EV_APP_UUID", "\"$evAppId\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(JavaVersion.VERSION_11.toString().toInt())
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    defaultConfig {
        aarMetadata {
            minCompileSdk = 26
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencyLocking {
    // Enable lock files for dependency versions.
    lockAllConfigurations()
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation(kotlin("test"))

    // Explicitly specify kotlin-stdlib-common version to ensure all configurations (including Android test)
    // are properly locked. Version must match the Kotlin plugin version in settings.gradle.kts (2.0.21).
    // This resolves the issue where debugAndroidTestRuntimeClasspath isn't captured during lockfile generation.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-common:2.2.21")

    // ktor
    implementation("io.ktor:ktor-client-core:3.4.0")
    implementation("io.ktor:ktor-client-okhttp:3.4.0")
    implementation("org.bouncycastle:bcprov-jdk15to18:1.78.1")

    // JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("com.google.code.gson:gson:2.8.9")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.evervault.sdk"
            artifactId = "evervault-core"
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
    // Load properties from local.properties
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }
    
    val signingKey: String? = localProperties.getProperty("signingKey")
    val signingPassword: String? = localProperties.getProperty("signingPassword")
    useInMemoryPgpKeys(signingKey ?: "", signingPassword ?: "")

    sign(publishing.publications["release"])
}
