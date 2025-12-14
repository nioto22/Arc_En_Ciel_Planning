import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            // Ktor Client
            implementation(libs.ktor.clientCore)
            implementation(libs.ktor.clientContentNegotiation)
            implementation(libs.ktor.clientSerialization)
            implementation(libs.ktor.clientLogging)
            implementation(libs.ktor.clientAuth)

            // SQLDelight
            implementation(libs.sqldelight.coroutines)

            // Kotlinx
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)

            // Koin
            implementation(libs.koin.core)
        }

        androidMain.dependencies {
            // Ktor Client Android
            implementation(libs.ktor.clientOkhttp)

            // SQLDelight Android Driver
            implementation(libs.sqldelight.driver.android)
        }

        iosMain.dependencies {
            // Ktor Client iOS
            implementation(libs.ktor.clientDarwin)

            // SQLDelight iOS Driver
            implementation(libs.sqldelight.driver.native)
        }

        jvmMain.dependencies {
            // SQLDelight JVM Driver
            implementation(libs.sqldelight.driver.jvm)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

sqldelight {
    databases {
        create("ArcEnCielDatabase") {
            packageName.set("com.aprouxdev.arcencielplanning.database")
        }
    }
}

android {
    namespace = "com.aprouxdev.arcencielplanning.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
