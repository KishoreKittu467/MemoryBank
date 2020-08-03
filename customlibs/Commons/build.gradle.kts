plugins {
    id(AppPlugins.library)
    kotlin(AppPlugins.android)
    kotlin(AppPlugins.androidExtensions)
    kotlin(AppPlugins.kapt)
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"
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

    sourceSets.getByName("main") {
        java.srcDir("src/main/kotlin")
    }
}

dependencies {
    Deps.allCommonImplDeps.forEach { implementation(it) }
    Deps.allCommonApiDeps.forEach { api(it) }
    kapt(Deps.glideCompiler)
}
