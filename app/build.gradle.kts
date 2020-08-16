plugins {
    id(AppPlugins.application)
    id(AppPlugins.timfreiheit)
    kotlin(AppPlugins.android)
    kotlin(AppPlugins.androidExtensions)
    kotlin(AppPlugins.kapt)
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        applicationId = "com.kkapps.memorybank"
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
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

    resourcePlaceholders {
        files = listOf("xml/shortcuts.xml")
    }
}

dependencies {
    Deps.allAppImplDeps.forEach { implementation(it) }
    Deps.allCommonKaptDeps.forEach { kapt(it) }
    CustomLibs.enabledCustomLibs.forEach { implementation(project(it)) }
//    CustomView.enabledCustomViews.forEach { implementation(project(it)) }
}
