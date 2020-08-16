plugins {
    id(AppPlugins.application)
}

android {
    compileSdkVersion(29)
    buildToolsVersion = "25.0.0"

    defaultConfig {
        applicationId = "com.eschao.android.widget.sample"
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
    implementation(project(CustomViews.pageFlip))
}
