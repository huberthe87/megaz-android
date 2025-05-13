import mega.privacy.android.build.preBuiltSdkDependency

plugins {
    alias(convention.plugins.mega.android.library)
    alias(convention.plugins.mega.android.library.compose)
    alias(convention.plugins.mega.android.test)
    alias(convention.plugins.mega.android.library.jacoco)
    alias(convention.plugins.mega.lint)
    alias(convention.plugins.mega.android.hilt)
    alias(plugin.plugins.de.mannodermaus.android.junit5)
    id("kotlin-android")
}

android {
    lint {
        disable += "CoroutineCreationDuringComposition"
        abortOnError = true
    }
    namespace = "mega.privacy.android.feature.devicecenter"
}

dependencies {
//    preBuiltSdkDependency(rootProject.extra)
    compileOnly(files("../../dependency/sdk-20250425.090537-rel.aar"))

    implementation(project(":core:formatter"))
    lintChecks(project(":lint"))

    implementation(project(":analytics"))
    implementation(project(":domain"))
    implementation(project(":navigation"))
    implementation(project(":data"))
    implementation(project(":shared:original-core-ui"))
    implementation(project(":shared:sync"))
    implementation(project(":shared:resources"))
    implementation(project(":legacy-core-ui"))
    implementation(project(":icon-pack"))

    testImplementation(project(":core-test"))
    testImplementation(project(":core-ui-test"))

//    implementation(lib.mega.analytics)
    implementation(project(":dependency"))
    implementation(lib.kotlin.ktx)
    implementation(lib.logging.timber)
    implementation(androidx.appcompat)
    implementation(androidx.fragment)
    implementation(google.material)
    implementation(androidx.lifecycle.viewmodel)
    implementation(androidx.lifecycle.service)

    // Compose
    implementation(androidx.lifecycle.runtime.compose)
    implementation(androidx.compose.activity)
    implementation(androidx.compose.viewmodel)
    implementation(androidx.bundles.compose.bom)
    implementation(lib.compose.state.events)
    implementation(androidx.hilt.navigation)
    implementation(androidx.constraintlayout.compose)
    implementation(lib.compose.state.events)

    testImplementation(testlib.bundles.ui.test)
    testImplementation(testlib.bundles.unit.test)

    testRuntimeOnly(testlib.junit.jupiter.engine)
    testImplementation(testlib.bundles.junit5.api)
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