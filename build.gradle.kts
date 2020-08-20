// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by extra("1.4.0")
    val kotlin_version by extra("1.4.0")
    repositories {
        google()
        jcenter()
        maven { url = uri(Repositories.gradle) }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
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
