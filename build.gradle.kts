// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    val kotlin_version by extra("1.3.72")
    repositories {
        google()
        jcenter()
        maven { url = uri(Repositories.gradle) }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        // NOTE: Do not place your application dependencies; here they belong
        // in the individual module build.gradle.kts.kts files
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

subprojects {
    if (name != "app") {
        apply(plugin = "common-scripts")
    }
}
