plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.kukai.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kukai.android"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    configurations {
        // Exclude bcprov-jdk15on module from all configurations
        all {
            exclude(group = "org.bouncycastle", module = "bcprov-jdk15on")
        }
    }
}

dependencies {
    implementation("androidx.browser:browser:1.4.0")
    implementation("org.torusresearch:web3auth-android-sdk:-SNAPSHOT")
    implementation("org.bouncycastle:bcprov-jdk15on:1.68")
    implementation("org.bitcoinj:bitcoinj-core:0.15.10")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}