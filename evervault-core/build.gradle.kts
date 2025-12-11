import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
}
android {
    group = "com.evervault.sdk.core"
    namespace = "com.evervault.sdk"
    compileSdk = 33
    val prop = Properties().apply {
        load(FileInputStream(File(rootProject.rootDir, "version.properties")))
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")

    implementation(kotlin("stdlib-common"))

    // ktor
    implementation("io.ktor:ktor-client-core:2.3.1")
    implementation("io.ktor:ktor-client-okhttp:2.3.1")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")

    // JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation("com.google.code.gson:gson:2.8.7")
    testImplementation(kotlin("test"))
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
