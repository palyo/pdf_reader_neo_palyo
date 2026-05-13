plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "coder.apps.aftercall"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "consumer-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Match the host app's catalog so versions stay consistent across modules.
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.multidex)
    implementation(libs.work.runtime)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.bundles.coroutines)

    // Codespace — provides BaseActivity + TinyDB used by the after-call code.
    implementation(libs.codespace)

    // AdMob (banner + native) — kept in lockstep with the host app.
    // Host pinned to 24.3.0 by request (was 25.2.0); mirror it here so the
    // aftercall module doesn't accidentally pull a newer transitive.
    implementation("com.google.android.gms:play-services-ads:24.3.0")

    // Firebase analytics for the firebaseASOEvent extension.
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    // Shimmer (loading skeleton in the native ad layout).
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // Fragment / RecyclerView / ViewPager2 / Startup — not in the version catalog.
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.startup:startup-runtime:1.2.0")
}
