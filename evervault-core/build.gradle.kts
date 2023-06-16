import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.8.21"
}

//android {
//    namespace = "com.evervault.sdk"
//    compileSdk = 33
//
//    defaultConfig {
//        minSdk = 26
//        targetSdk = 33
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}

//dependencies {
//    implementation("androidx.core:core-ktx:1.8.0")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.5.0")


    // crypto
//    implementation("org.bouncycastle:bcprov-jdk15on:1.70")

//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//}

val ktorVersion = "2.3.1"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    val platforms: List<KotlinNativeTarget> = emptyList()
//    val platforms: List<KotlinNativeTarget> = listOf(
//        macosX64(),
//        macosArm64(),
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64(),
//        watchosX64(),
//        watchosArm32(),
//        watchosArm64(),
//        watchosSimulatorArm64(),
//        tvosX64(),
//        tvosArm64(),
//        tvosSimulatorArm64()
//    )

    sourceSets {
        val commonMain by getting {
            dependencies {
                // ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
//                implementation("io.ktor:ktor-client-okhttp:2.3.1")

                // JSON
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

//                testImplementation("junit:junit:4.13.2")
//                testImplementation(kotlin("test-junit"))

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
                implementation("org.bouncycastle:bcprov-jdk15on:1.70")
            }
        }

        val platformMain = platforms.map { sourceSets.getByName("${it.name}Main") }
        val platformTest = platforms.map { sourceSets.getByName("${it.name}Test") }

        platformMain.forEach {
            it.dependsOn(commonMain)
        }

        platformTest.forEach {
            it.dependsOn(commonTest)
        }
    }
}