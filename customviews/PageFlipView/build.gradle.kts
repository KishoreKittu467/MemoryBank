plugins {
    id(AppPlugins.library)
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "25.0.0"

    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0.2"

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
    implementation("com.android.support:appcompat-v7:25.0.0")
    testImplementation(Deps.junit)
}
