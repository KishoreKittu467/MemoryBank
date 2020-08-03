// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        jcenter()
        maven { url = uri(Repositories.gradle) }
    }

    dependencies {
        // NOTE: Do not place your application dependencies; here they belong
        // in the individual module build.gradle files
        ProjectPlugins.allProjectLevelPlugins.forEach { classpath(it) }
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri(Repositories.jitpack) }
    }
}
