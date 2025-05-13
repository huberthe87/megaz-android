plugins {
    alias(convention.plugins.mega.android.library)
    alias(convention.plugins.mega.android.library.compose)
    alias(convention.plugins.mega.android.room)
    alias(convention.plugins.mega.android.test)
    alias(convention.plugins.mega.android.library.jacoco)
    alias(convention.plugins.mega.lint)
    alias(convention.plugins.mega.android.hilt)
    alias(plugin.plugins.de.mannodermaus.android.junit5)
    alias(plugin.plugins.kotlin.serialisation)
    id("kotlin-android")
}

android {
    namespace = "mega.privacy.android.feature.settings"
    testOptions {
        unitTests {
            targetSdk = 34
        }
    }
}

dependencies {
    implementation(project(":icon-pack"))
    implementation(project(":shared:resources"))
    implementation(project(":domain"))
//    implementation(lib.mega.core.ui)
    implementation(project(":dependency"))
    implementation(androidx.material3.window)
    implementation(androidx.bundles.compose.bom)
    implementation(androidx.material3)
    implementation(androidx.hilt.navigation)
    implementation(lib.kotlin.serialisation)
    implementation(androidx.navigation.compose)

    //test
    testImplementation(project(":core-test"))
    testImplementation(project(":core-ui-test"))
    testImplementation(platform(testlib.junit5.bom))
    testImplementation(testlib.bundles.ui.test)
    testImplementation(testlib.bundles.unit.test)
    testImplementation(testlib.bundles.junit5.api)
    testImplementation(google.hilt.android.test)
    testRuntimeOnly(testlib.junit.jupiter.engine)
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