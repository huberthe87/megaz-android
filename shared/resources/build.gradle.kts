plugins {
    alias(convention.plugins.mega.android.library)
    alias(convention.plugins.mega.lint)
    id("kotlin-android")
}

android {
    namespace = "mega.privacy.android.shared.resources"

    dependencies {
        lintChecks(project(":lint"))
    }
}

// Set Kotlin JVM target specifically for this module
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
