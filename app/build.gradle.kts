plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.firebase.crashlitycs)
    alias(libs.plugins.gms.googleServices)
}

android {
    namespace = "com.pdf.read.view.pdfreader.pdfviewer.pdfeditor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pdf.read.view.pdfreader.pdfviewer.pdfeditor"
        minSdk = 26
        targetSdk = 35
        versionCode = 5
        versionName = "5.0"

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
    }
    kotlin {
        jvmToolchain(17)
    }
    configurations.all {
        resolutionStrategy {
            force("org.bouncycastle:bcpkix-jdk15to18:1.72")
            force("org.bouncycastle:bcprov-jdk15to18:1.72")
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
    }

    configurations {
        implementation {
            exclude(group = "com.squareup.okio", module = "okio")
        }
    }
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

    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation(libs.pdfbox.android)
    implementation(libs.pdfium.android)
    implementation(libs.itextg)
    implementation("com.aspose:aspose-words:20.6:android.via.java")
    implementation("com.aspose:aspose-cells:20.6:android.via.java")

    implementation("com.github.ome450901:SimpleRatingBar:1.5.1")

    implementation("com.google.android.gms:play-services-ads:23.6.0")

    implementation("com.google.ads.mediation:applovin:13.3.1.1")
    implementation("com.google.ads.mediation:inmobi:10.8.3.1")
    implementation("com.google.ads.mediation:ironsource:8.10.0.0")
    implementation("com.google.ads.mediation:vungle:7.5.0.1")
    implementation("com.google.ads.mediation:facebook:6.20.0.0")
    implementation("com.unity3d.ads:unity-ads:4.15.0")
    implementation("com.google.ads.mediation:unity:4.15.1.0")
}