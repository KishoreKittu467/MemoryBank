plugins {
    id(AppConfig.AppPlugins.application)
    kotlin(AppConfig.AppPlugins.android)
    kotlin(AppConfig.AppPlugins.androidExtensions)
    kotlin(AppConfig.AppPlugins.kapt)
}

android {
    compileSdkVersion(AppConfig.Versions.compileSdkVersion)
    buildToolsVersion = AppConfig.Versions.buildToolsVersion

    defaultConfig {
        applicationId = "com.kkapps.bubbleheadsdemo"
        minSdkVersion(AppConfig.Versions.minSdkVersion)
        targetSdkVersion(AppConfig.Versions.targetSdkVersion)
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
}

dependencies {
    implementation(project(CustomView.BUBBLE_HEADS))
    implementation(Deps.appCompat)
    implementation(Deps.material)
    implementation(Deps.glide)
    kapt(Deps.glideCompiler)
}