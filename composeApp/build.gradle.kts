
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "onthegobingo.composeapp.generated.resources"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xopt-in=kotlin.time.ExperimentalTime")
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.androidx.splash)
            implementation(libs.androidx.activity.compose)
            implementation(libs.google.auth)
        }
        iosMain.dependencies {
            // TODO we probably don't need this
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(compose.components.resources)
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

tasks.named("embedAndSignAppleFrameworkForXcode") {
    val configuration = System.getenv("CONFIGURATION")?.uppercase() ?: "DEBUG"
    val platformName = System.getenv("PLATFORM_NAME") ?: "iphonesimulator"

    val buildTaskName =
        when {
            platformName == "iphonesimulator" -> "linkDebugFrameworkIosSimulatorArm64"
            configuration == "RELEASE" -> "linkReleaseFrameworkIosArm64"
            else -> "linkDebugFrameworkIosArm64"
        }

    logger.lifecycle("Depending on build task: $buildTaskName")
    dependsOn(buildTaskName)
}

android {
    namespace = "com.otg.bingo"
    compileSdk =
        libs.versions.android.compileSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.otg.bingo"
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.android.targetSdk
                .get()
                .toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
