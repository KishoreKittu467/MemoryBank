plugins {
    id(AppPlugins.application)
    kotlin(AppPlugins.android)
    kotlin(AppPlugins.androidExtensions)
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        applicationId = "com.kkapps.autolinkstextviewdemo"
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
}

dependencies {
    implementation(project(CustomViews.autoLink))
    implementation(Deps.constraintLayout)
    implementation(Deps.appCompat)
    implementation(Deps.material)
    testImplementation(Deps.junit)
    androidTestImplementation(Deps.espresso)
}

