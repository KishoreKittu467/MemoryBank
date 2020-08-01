plugins {
    id(AppConfig.AppPlugins.library)
    kotlin(AppConfig.AppPlugins.android)
    kotlin(AppConfig.AppPlugins.androidExtensions)
    kotlin(AppConfig.AppPlugins.kapt)
}

android {
    compileSdkVersion(AppConfig.Versions.compileSdkVersion)
    buildToolsVersion = AppConfig.Versions.buildToolsVersion

    defaultConfig {
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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.stdlib)
    implementation(Deps.coreKTX)
    implementation(Deps.appCompat)
    implementation(Deps.material)

    testImplementation(Deps.junit)
    androidTestImplementation(Deps.junitExt)
    androidTestImplementation(Deps.espresso)
}