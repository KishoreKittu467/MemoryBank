// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        jcenter()
        maven { url = uri(AppConfig.Repositories.gradle) }
    }

    dependencies {
        "classpath"("com.android.tools.build:gradle:4.0.1")
        // NOTE: Do not place your application dependencies; here they belong
        // in the individual module build.gradle files
        AppConfig.ProjectPlugins.allProjectLevelPlugins.forEach { classpath(it) }
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri(AppConfig.Repositories.jitpack) }
    }
}
