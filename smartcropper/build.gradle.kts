plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "me.pqpo.smartcropperlib"
    compileSdk = 34
    ndkVersion = "27.2.12479018"

    defaultConfig {
        minSdk = 26
        targetSdk = 34

        externalNativeBuild {
            cmake {
                cppFlags.add("-std=c++11")
                cppFlags.add("-frtti")
                cppFlags.add("-fexceptions")
                abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
                arguments.addAll(
                    listOf(
                        "-DANDROID_TOOLCHAIN=clang",
                        "-DANDROID_STL=c++_static",
                        "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON"
                    )
                )
            }
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }

    lintOptions {
        isAbortOnError = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        // LiteRT 2.1.4 was compiled with Kotlin 2.3.0 metadata; this project is on
        // Kotlin 2.1.10. The bytecode is forward-compatible — only the metadata
        // version differs — so skip the strict check on third-party class files.
        freeCompilerArgs += "-Xskip-metadata-version-check"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.tensorflow.lite)
    implementation(libs.androidx.core.ktx)
}
