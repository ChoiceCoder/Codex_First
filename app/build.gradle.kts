import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

val defaultSupabaseUrl = "https://your-project-ref.supabase.co"
val defaultSupabaseAnonKey = "your-anon-key"

val localProperties = Properties().apply {
    listOf(rootProject.file("local.properties"), project.file("local.properties")).forEach { file ->
        if (file.exists()) {
            file.inputStream().use(::load)
        }
    }
}

fun sanitizeSecret(raw: String?): String? =
    raw
        ?.trim()
        ?.removeSurrounding("\"")
        ?.removeSurrounding("'")
        ?.takeIf { it.isNotBlank() }

fun resolveSecret(vararg names: String, default: String): String {
    names.forEach { name ->
        sanitizeSecret(project.findProperty(name)?.toString())?.let { return it }
        sanitizeSecret(localProperties.getProperty(name))?.let { return it }
        sanitizeSecret(System.getenv(name))?.let { return it }
    }
    return default
}

val supabaseUrl = resolveSecret(
    "SUPABASE_URL",
    "supabase.url",
    "supabaseUrl",
    "GOVTPREP_SUPABASE_URL",
    default = defaultSupabaseUrl
)
val supabaseAnonKey = resolveSecret(
    "SUPABASE_ANON_KEY",
    "supabase.anon.key",
    "supabaseAnonKey",
    "GOVTPREP_SUPABASE_ANON_KEY",
    default = defaultSupabaseAnonKey
)

android {
    namespace = "com.govtprep"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.govtprep"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "SUPABASE_URL", "\"$supabaseUrl\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"$supabaseAnonKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation(platform("io.github.jan-tennert.supabase:bom:2.5.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:gotrue-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("androidx.compose.animation:animation")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
