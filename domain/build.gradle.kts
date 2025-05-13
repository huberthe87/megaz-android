plugins {
    alias(convention.plugins.mega.jvm.library)
    alias(convention.plugins.mega.jvm.test)
    alias(convention.plugins.mega.jvm.jacoco)
    alias(convention.plugins.mega.lint)
    alias(convention.plugins.mega.jvm.hilt)
    alias(plugin.plugins.kotlin.serialisation)
}

lint {
    abortOnError = true
    warningsAsErrors = true
}

dependencies {
    lintChecks(project(":lint"))
    implementation(lib.coroutines.core)
    implementation(lib.javax.inject)

    implementation(androidx.paging.core)

    implementation(lib.kotlin.serialisation)

    // Testing dependencies
    testImplementation(testlib.bundles.unit.test)
    testImplementation(lib.bundles.unit.test)
    testImplementation(platform(testlib.junit5.bom))
    testImplementation(testlib.bundles.junit5.api)
    testRuntimeOnly(testlib.junit.jupiter.engine)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_21.toString()
    targetCompatibility = JavaVersion.VERSION_21.toString()
}