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
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    Deps.allCommonApiDeps.forEach { api(it) }
    Deps.allCommonKaptDeps.forEach { kapt(it) }
    Deps.allCommonTestImplDeps.forEach { testImplementation(it) }
    Deps.allCommonAndroidTestImplDeps.forEach { androidTestImplementation(it) }
}
repositories {
    google()
}
