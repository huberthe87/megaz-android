import mega.privacy.android.build.shouldApplyDefaultConfiguration

plugins {
    alias(convention.plugins.mega.android.library)
    alias(convention.plugins.mega.android.library.compose)
    alias(convention.plugins.mega.lint)
    alias(convention.plugins.mega.android.hilt)
    alias(plugin.plugins.de.mannodermaus.android.junit5)
}

android {
    namespace = "mega.privacy.android.analytics"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    lintChecks(project(":lint"))
    implementation(project(":domain"))

    // Analytics
//    implementation(lib.mega.analytics)
    implementation(project(":dependency"))

    // DI
    implementation(lib.javax.inject)

    // Framework
    implementation(androidx.bundles.compose.bom)
    implementation(lib.kotlin.ktx)
    implementation(androidx.appcompat)

    // Logging
    implementation(lib.bundles.logging)

    // Testing
    testImplementation(testlib.junit)
    testImplementation(platform(testlib.junit5.bom))
    testImplementation(testlib.bundles.junit5.api)
    testRuntimeOnly(testlib.junit.jupiter.engine)
    testImplementation(testlib.junit.test.ktx)

    testImplementation(testlib.bundles.unit.test)

    testImplementation(testlib.mockito)
    testImplementation(testlib.mockito.kotlin)
    testImplementation(testlib.mockito.android)
}

// Set KSP JVM target specifically for this module
tasks.withType<com.google.devtools.ksp.gradle.KspTaskJvm>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

// Set Kotlin JVM target specifically for this module
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}