import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(localFile))
    }
}

val evAppId: String = localProperties.getProperty("EV_APP_UUID") ?: ""
val evervaultMerchantId: String = localProperties.getProperty("MERCHANT_ID") ?: ""
val evTeamId: String = localProperties.getProperty("EV_TEAM_UUID") ?: ""
val evervaultEnclaveId: String = localProperties.getProperty("ENCLAVE_UUID") ?: ""
val evervaultPcrCallbackUrl: String = localProperties.getProperty("PCR_CALLBACK_URL") ?: ""
val evervaultEnclaveUrl: String = localProperties.getProperty("ENCLAVE_URL") ?: ""

android {
    namespace = "com.evervault.sampleapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.evervault.sampleapplication"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "ENCLAVE_UUID", "\"$evervaultEnclaveId\"")
        buildConfigField("String", "EV_TEAM_UUID", "\"$evTeamId\"")
        buildConfigField("String", "EV_APP_UUID", "\"$evAppId\"")
        buildConfigField("String", "MERCHANT_ID", "\"$evervaultMerchantId\"")
        buildConfigField("String", "PCR_CALLBACK_URL", "\"$evervaultPcrCallbackUrl\"")
        buildConfigField("String", "ENCLAVE_URL", "\"$evervaultEnclaveUrl\"")
    }

    lint {
        targetSdk = 36
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(JavaVersion.VERSION_11.toString().toInt())
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencyLocking {
    // Enable lock files for dependency versions.
    lockAllConfigurations()
}

dependencies {
    implementation(project(":evervault-inputs"))
    implementation(project(":evervault-enclaves"))
    implementation(project(":evervault-core"))
    implementation("androidx.core:core-ktx:1.8.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.24"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation(platform("androidx.compose:compose-bom:2022.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    // GSON converter
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.5.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0-alpha03")
    // Explicitly specify kotlin-stdlib-common version to ensure debugAndroidTestRuntimeClasspath is properly locked.
    // Version must match the Kotlin plugin version in settings.gradle.kts (2.2.21).
    androidTestImplementation("org.jetbrains.kotlin:kotlin-stdlib-common:2.2.21")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
