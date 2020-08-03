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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    CustomView.allCustomViews.forEach { api(project(it)) }
    Deps.allAppImplDeps.forEach { implementation(it) }
    Deps.allAppApiDeps.forEach { api(it) }
    Deps.allAppKaptDeps.forEach { kapt(it) }
    Deps.allAppTestImplDeps.forEach { testImplementation(it) }
    Deps.allAppAndroidTestImplDeps.forEach { androidTestImplementation(it) }
}
