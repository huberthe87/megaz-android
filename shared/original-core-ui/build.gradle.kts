plugins {
    alias(convention.plugins.mega.android.library)
    alias(convention.plugins.mega.android.library.compose)
    alias(convention.plugins.mega.android.test)
    alias(convention.plugins.mega.lint)
    alias(convention.plugins.mega.android.library.jacoco)
    alias(convention.plugins.mega.android.hilt)
    id("kotlin-android")
}

android {
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

    lint {
        disable += "CoroutineCreationDuringComposition"
        abortOnError = true
    }

    namespace = "mega.privacy.android.core"
}

dependencies {
    lintChecks(project(":lint"))

    implementation(project(":icon-pack"))
    implementation(project(":shared:resources"))
    testImplementation(project(":core-ui-test"))

    implementation(androidx.constraintlayout.compose)
    implementation(androidx.bundles.compose.bom)
    implementation(lib.kotlin.ktx)
    implementation(androidx.appcompat)
    implementation(google.material)
    implementation(google.accompanist.systemui)
    implementation(google.accompanist.permissions)
    implementation(google.accompanist.navigationmaterial)
    implementation(androidx.compose.activity)
    implementation(androidx.lifecycle.runtime)
    implementation(androidx.lifecycle.runtime.compose)
    implementation(lib.compose.state.events)
    implementation(androidx.emojiPicker)
    implementation(lib.coil.compose)
    implementation(lib.balloon)
    implementation(google.accompanist.placeholder)
//    api(lib.mega.core.ui)
//    implementation(lib.mega.core.ui.tokens)
    api(project(":dependency"))
    implementation(lib.kotlinx.collections.immutable)

    debugImplementation(google.gson)
    debugImplementation(lib.kotlinpoet)

    testImplementation(testlib.bundles.ui.test)
    testImplementation(testlib.bundles.unit.test)
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