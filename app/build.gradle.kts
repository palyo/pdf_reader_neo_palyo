plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.firebase.crashlitycs)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.gms.googleServices)
}

android {
    // ── Firebase App Distribution ────────────────────────────────────────────
    val appDistCredsFile = rootProject.file("appdistribution/service-account.json")

    // Default tester groups (Firebase App Distribution → Groups → group alias).
    // Comma-separate for multiple. The group must already exist in the Firebase
    // Console — using a non-existent alias makes the upload fail with
    // "[404] Requested entity was not found."
    val defaultAppDistGroups = ""

    // Default individual testers — comma-separated emails. Leave empty to only
    // use groups.
    val defaultAppDistTesters = "shaunmichelle23@gmail.com,thoriyaprahalad@gmail.com"

    val appDistGroups: String =
        (project.findProperty("firebaseAppDistributionGroups") as String?)
            ?: System.getenv("FIREBASE_APP_DISTRIBUTION_GROUPS")
            ?: defaultAppDistGroups
    val appDistTesters: String =
        (project.findProperty("firebaseAppDistributionTesters") as String?)
            ?: System.getenv("FIREBASE_APP_DISTRIBUTION_TESTERS")
            ?: defaultAppDistTesters

    // ── Release signing ──────────────────────────────────────────────────────
    // The keystore `pdf_reader.jks` lives at the project root. Override alias /
    // passwords via Gradle properties or env vars so CI doesn't need to touch
    // this file. Defaults assume alias and both passwords match the keystore name.
    val releaseKeystoreFile = rootProject.file("pdf_reader.jks")
    val defaultSigningSecret = "pdf_reader"
    val releaseStorePassword: String =
        (project.findProperty("RELEASE_STORE_PASSWORD") as String?)
            ?: System.getenv("RELEASE_STORE_PASSWORD")
            ?: defaultSigningSecret
    val releaseKeyAlias: String =
        (project.findProperty("RELEASE_KEY_ALIAS") as String?)
            ?: System.getenv("RELEASE_KEY_ALIAS")
            ?: defaultSigningSecret
    val releaseKeyPassword: String =
        (project.findProperty("RELEASE_KEY_PASSWORD") as String?)
            ?: System.getenv("RELEASE_KEY_PASSWORD")
            ?: defaultSigningSecret

    namespace = "com.pdf.read.view.pdfreader.pdfviewer.pdfeditor"
    compileSdk = 35
    ndkVersion = "27.2.12479018"

    signingConfigs {
        create("release") {
            if (releaseKeystoreFile.exists()) storeFile = releaseKeystoreFile
            storePassword = releaseStorePassword
            keyAlias = releaseKeyAlias
            keyPassword = releaseKeyPassword
        }
    }

    defaultConfig {
        applicationId = "com.pdf.read.view.pdfreader.pdfviewer.pdfeditor"
        minSdk = 26
        targetSdk = 35
        versionCode = 10
        versionName = "10.0"

        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        setProperty("archivesBaseName", "Pdf Reader - Pdf Viewer v$versionName")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resConfigs("en", "hi", "de", "fr", "ar", "ja", "es", "in", "af", "pt")
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    buildTypes {
        debug {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            firebaseAppDistribution {
                // AAB requires the Firebase project to be linked to a Google Play
                // account (Firebase Console → Project Settings → Integrations →
                // Google Play → Link) **or** to have the ad-hoc bundle sharing key
                // generated AND registered (Firebase Console → App Distribution →
                // Settings → Android App Bundles). Without either, the upload step
                // fails with "App Distribution failed to process the AAB" and the
                // info log shows `integrationState: ADHOC_SHARING_KEY_NOT_*`.
                // Switch back to "APK" if neither integration is available — APK
                // uploads work without any Console setup.
                artifactType = "AAB"
                if (appDistCredsFile.exists()) {
                    serviceCredentialsFile = appDistCredsFile.absolutePath
                }
                if (appDistGroups.isNotBlank()) groups = appDistGroups
                if (appDistTesters.isNotBlank()) testers = appDistTesters
                releaseNotes = "v${defaultConfig.versionName}"
            }
        }
    }
    bundle {
        language {
            enableSplit = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        // LiteRT 2.1.4 (transitive via :smartcropper) ships Kotlin 2.3.0 metadata;
        // this project is on Kotlin 2.1.10. Bytecode is forward-compatible —
        // only metadata differs — so skip the strict version check.
        freeCompilerArgs += "-Xskip-metadata-version-check"
    }
    kotlin {
        jvmToolchain(17)
    }
    configurations.all {
        resolutionStrategy {
            force("org.bouncycastle:bcpkix-jdk15to18:1.72")
            force("org.bouncycastle:bcprov-jdk15to18:1.72")
            // Keep the 4 KB-aligned barteksc/mhiew pdfium-android out of the graph.
            // We pull `io.github.oothp:pdfium-android:1.9.5-beta01` explicitly below;
            // that fork preserves the `com.shockwave.pdfium.*` API and ships 16 KB-aligned
            // libjniPdfium / libmodpdfium / libmodft2 / libmodpng / libc++_shared.
            exclude(group = "com.github.barteksc", module = "pdfium-android")
            exclude(group = "com.github.mhiew", module = "pdfium-android")
        }
    }
    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/NOTICE.md")
        resources.pickFirsts.add("com/itextpdf/io/font/*")
        resources.pickFirsts.add("com/itextpdf/io/font/cmap/*")
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "org/bouncycastle/x509/CertPathReviewerMessages_de.properties"
        }
        jniLibs {
            // Required by Android 15+ 16 KB page size: store .so uncompressed
            // and let zipalign/bundletool page-align them in the final APK.
            useLegacyPackaging = false
            // gpuimage 2.0.4 ships a 4 KB-aligned libyuv-decoder.so used only by
            // GPUImage's YUV camera-preview path (`updatePreviewFrame(byte[]…)` /
            // `GPUImageNativeLibrary`). This app only feeds GPUImage Bitmaps, so the
            // .so is never dlopen'd at runtime — exclude it to clear the warning.
            excludes += "**/libyuv-decoder.so"
        }
    }

    configurations {
        implementation {
            exclude(group = "com.squareup.okio", module = "okio")
        }
    }
}

// One-shot task: builds the release AAB and uploads it to Firebase App
// Distribution. `bundleRelease` produces the signed Android App Bundle that
// `appDistributionUploadRelease` then ships. Mirrors the same task pattern
// used in MagicVoiceRecorder / Kontacts. Swap `bundleRelease` for
// `assembleRelease` if you change `artifactType` back to "APK" above.
tasks.register("uploadReleaseToFirebase") {
    group = "distribution"
    description = "Builds the release AAB and uploads it to Firebase App Distribution."
    dependsOn("bundleRelease", "appDistributionUploadRelease")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.codespace)

    implementation(libs.pdfviewer)
    implementation(libs.lottie)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.gson)
    implementation(libs.multidex)
    implementation(libs.preference)
    implementation(libs.browser)

    implementation(libs.work.runtime)

    implementation(libs.bundles.glide)
    ksp(libs.glide.ksp)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.navigation)

    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.guava)
    implementation(libs.gpuimage)

    implementation(project(":smartcropper"))
    implementation(project(":aftercall"))
    implementation(libs.jp.wasabeef.recyclerview.animators)
    implementation(libs.jp.wasabeef.glide.transformations)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.messaging.ktx)

    implementation(libs.spongycastle.core) { exclude(group = "org.apache.xmlbeans", module = "xmlbeans") }
    implementation(libs.spongycastle.prov)
    implementation(libs.xmlbeans)
    implementation(libs.poi) { exclude(group = "org.apache.xmlbeans", module = "xmlbeans") }
    implementation(libs.poi.ooxml)
    implementation(libs.poi.ooxml.schemas)
    implementation(libs.poi.scratchpad)

    // android-pdf-viewer (Java only, no native code). The mhiew fork keeps the
    // same `com.github.barteksc.pdfviewer.*` package & public API as upstream barteksc.
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")
    // 16 KB-aligned pdfium native libs. Drop-in for `com.shockwave.pdfium.*` API.
    // Replaces the abandoned 4 KB-aligned barteksc:pdfium-android:1.9.0 / mhiew:1.9.2.
    implementation("io.github.oothp:pdfium-android:1.9.5-beta01")
    implementation(libs.pdfbox.android)
    implementation(libs.itextg)
    implementation("com.aspose:aspose-words:20.6:android.via.java")
    implementation("com.aspose:aspose-cells:20.6:android.via.java")

    implementation("com.github.ome450901:SimpleRatingBar:1.5.1")

    // Downgraded from 25.2.0 → 24.3.0 by request. 25.x had Android 16
    // edge-to-edge handling built in but introduced render/UX changes the
    // team didn't want yet. 24.3.0 is the stable mid-24.x release that the
    // mediation adapter versions below were originally certified against.
    implementation("com.google.android.gms:play-services-ads:24.3.0")

    implementation("com.google.ads.mediation:applovin:13.3.1.1")
    implementation("com.google.ads.mediation:inmobi:10.8.3.1")
    implementation("com.google.ads.mediation:ironsource:8.10.0.0")
    implementation("com.google.ads.mediation:vungle:7.5.0.1")
    implementation("com.google.ads.mediation:facebook:6.20.0.0")
    implementation("com.unity3d.ads:unity-ads:4.15.0")
    implementation("com.google.ads.mediation:unity:4.15.1.0")

    implementation("com.github.akshaaatt:Google-IAP:1.8.0")
}