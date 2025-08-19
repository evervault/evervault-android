import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.8.21"
}

// Load properties from local.properties
val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(localFile))
    }
}

val evApiKey: String = localProperties.getProperty("EV_API_KEY") ?: ""
val evAppUuid: String = localProperties.getProperty("EV_APP_UUID") ?: ""
val evTeamUuid: String = localProperties.getProperty("EV_TEAM_UUID") ?: ""

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

android {
    namespace = "com.evervault.sdk.e2e"
    compileSdk = 33

    defaultConfig {
        minSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        
        // Build config fields for E2E test configuration
        buildConfigField("String", "EV_API_KEY", "\"$evApiKey\"")
        buildConfigField("String", "EV_APP_UUID", "\"$evAppUuid\"")
        buildConfigField("String", "EV_TEAM_UUID", "\"$evTeamUuid\"")
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
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

val ktorVersion = "2.3.1"

dependencies {
    implementation(project(":evervault-common"))
    implementation(kotlin("stdlib-common"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.google.code.gson:gson:2.8.7")
    testImplementation("junit:junit:4.13.2")
}
