plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.aprouxdev.arcencielplanning"
version = "1.0.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    // Shared module
    implementation(projects.shared)

    // Ktor Server Core
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)

    // Ktor Plugins
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serverWebsockets)
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.serverAuthJwt)
    implementation(libs.ktor.serverCors)
    implementation(libs.ktor.serverCallLogging)
    implementation(libs.ktor.serverDefaultHeaders)
    implementation(libs.ktor.serverStatusPages)

    // Serialization
    implementation(libs.ktor.serializationKotlinxJson)
    implementation(libs.kotlinx.serialization.json)

    // Logging
    implementation(libs.logback)

    // Configuration
    implementation(libs.ktor.serverConfigYaml)

    // Testing
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}