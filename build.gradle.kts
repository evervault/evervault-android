plugins {
    kotlin("jvm") version "1.8.21" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

//plugins {
//    kotlin("jvm") version "1.8.21"
//    id("com.android.application") version "8.0.2" apply false
//    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
////    application
//}

//group = "com.evervault"
//version = "1.0-SNAPSHOT"

//dependencies {
//    testImplementation(kotlin("test"))
//}

//tasks.test {
//    useJUnitPlatform()
//}

//kotlin {
//    jvmToolchain(11)
//}
