plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
}

android {
    group = "com.evervault.sdk"
    namespace = "com.evervault.sdk.cages"
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packagingOptions {
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
    implementation("com.evervault.sdk:evervault-core:1.0")

    implementation("androidx.core:core-ktx:1.8.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("net.java.dev.jna:jna:5.7.0@aar")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("junit:junit:4.12")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.evervault.sdk"
            artifactId = "evervault-cages"
            version = "1.2"

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
