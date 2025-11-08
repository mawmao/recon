import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

val supabaseProperties = Properties()
val supabasePropertiesFile = rootProject.file("supabase.properties")
supabaseProperties.load(FileInputStream(supabasePropertiesFile))

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp )
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.kt.serialization)
}

android {
    namespace = "com.maacro.recon"
    compileSdk = 36

    room {
        schemaDirectory("$projectDir/schemas")
        generateKotlin = true
    }

    defaultConfig {
        applicationId = "com.maacro.recon"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.1"
    }

    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            buildConfigField(
                "String",
                "SUPABASE_URL",
                "\"${supabaseProperties.getProperty("SUPABASE_URL_DEV")}\""
            )
            buildConfigField(
                "String",
                "SUPABASE_KEY",
                "\"${supabaseProperties.getProperty("SUPABASE_KEY_DEV")}\""
            )
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("prod") {
            dimension = "environment"
            buildConfigField(
                "String",
                "SUPABASE_URL",
                "\"${supabaseProperties.getProperty("SUPABASE_URL_PROD")}\""
            )
            buildConfigField(
                "String",
                "SUPABASE_KEY",
                "\"${supabaseProperties.getProperty("SUPABASE_KEY_PROD")}\""
            )
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.named("debug").get()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}


dependencies {
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.ktor.client.okhttp)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest.kt)
    implementation(libs.supabase.auth.kt)
    implementation(libs.supabase.realtime.kt)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.mlkit.vision)
    implementation(libs.barcode.scanning)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.hilt.android)
    implementation(libs.hilt.work)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)

    implementation(libs.serialization.json)

    implementation(libs.androidx.compose.navigation)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.ext)

    implementation(project(":forms:model"))
    implementation(project(":forms:generator"))
    ksp(project(":forms:generator"))

    implementation(libs.timber)
}