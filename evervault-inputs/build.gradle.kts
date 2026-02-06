import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
    id("signing")
}


android {
    group = "com.evervault.sdk"
    namespace = "com.evervault.sdk.inputs"
    compileSdk = 36
    val prop = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "version.properties")))
    }
    version = prop.getProperty("VERSION_NAME")
    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

val composeActivityVersion = "1.9.0"

dependencies {
    api("com.github.evervault:evervault-pay:android-v0.0.28")
    implementation(project(":evervault-core"))
    implementation("androidx.core:core-ktx:1.12.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:${composeActivityVersion}")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation-layout")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.evervault.sdk"
            artifactId = "evervault-inputs"
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
