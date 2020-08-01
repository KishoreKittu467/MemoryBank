plugins {
    id(AppConfig.AppPlugins.application)
    id(AppConfig.AppPlugins.timfreiheit)
    kotlin(AppConfig.AppPlugins.android)
    kotlin(AppConfig.AppPlugins.androidExtensions)
    kotlin(AppConfig.AppPlugins.kapt)
}

android {
    compileSdkVersion(AppConfig.Versions.compileSdkVersion)
    buildToolsVersion = AppConfig.Versions.buildToolsVersion

    defaultConfig {
        applicationId = "com.kkapps.memorybank"
        minSdkVersion(AppConfig.Versions.minSdkVersion)
        targetSdkVersion(AppConfig.Versions.targetSdkVersion)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    resourcePlaceholders {
        files = listOf("xml/shortcuts.xml")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    CustomView.allCustomViews.forEach { api(project(it)) }
    Deps.allAppImplDeps.forEach { implementation(it) }
    Deps.allAppApiDeps.forEach { api(it) }
    Deps.allAppKaptDeps.forEach { kapt(it) }
    Deps.allAppTestImplDeps.forEach { testImplementation(it) }
    Deps.allAppAndroidTestImplDeps.forEach { androidTestImplementation(it) }
}
