plugins {
    id(AppPlugins.library)
    kotlin(AppPlugins.android)
    kotlin(AppPlugins.androidExtensions)
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = Versions.compileJavaVersion
        targetCompatibility = Versions.compileJavaVersion
    }

    kotlinOptions {
        jvmTarget = Versions.targetJavaVersion
    }
}

dependencies {
    api(project(CustomLibs.libCommons))
}