plugins {
    id("bandlab.buildbenchmark.app")
}

android {
    namespace = "bandlab.buildbenchmark"
    compileSdk = 35

    defaultConfig {
        applicationId = "bandlab.buildbenchmark"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":login"))
    implementation(project(":toaster"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.feature)

    implementation(libs.dagger)
    kapt(libs.daggerCompiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
