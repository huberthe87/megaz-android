plugins {
    alias(convention.plugins.mega.android.library)
}

android {
    namespace = "mega.privacy.android.core.test"

    packaging {
        resources.excludes.add("/META-INF/*")
    }
}

dependencies {
    // Coroutines
    implementation(lib.coroutines.test)

    implementation(project(":analytics"))
    // JUnit5
    implementation(platform(testlib.junit5.bom))
    implementation(testlib.junit.test.ktx)
    implementation(testlib.junit.jupiter.api)
//    implementation(lib.mega.analytics)
    implementation(project(":dependency"))
}

// Set Kotlin JVM target specifically for this module
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
