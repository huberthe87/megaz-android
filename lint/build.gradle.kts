plugins {
    alias(convention.plugins.mega.jvm.library)
    id("com.android.lint")
}

lint {
    htmlReport = true
    htmlOutput = file("lint-report.html")
    textReport = true
    absolutePaths = false
    ignoreTestSources = true
}

dependencies {
    compileOnly(lib.kotlin.stdlib.jdk7)
    // For a description of the below dependencies, see the main project README
    compileOnly(tools.lint.api)
    // You typically don't need this one:
    compileOnly(tools.lint.checks)

    testImplementation(testlib.junit)
    testImplementation(tools.lint)
    testImplementation(tools.lint.tests)
}

// Set Kotlin JVM target specifically for this module
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
