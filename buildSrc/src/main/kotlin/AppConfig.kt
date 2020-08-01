object AppConfig {

    object Apps {
        //Do not make these private even after auto suggestion
        // TODO: 20/07/20 try to add these to settings.gradle.kts
        const val APP = ":app"
        const val DEMO_BUBBLE_HEADS = ":BubbleHeadsDemo"
        const val DEMO_PAGE_FLIP = ":PageFlipDemo"

        val allDemoApps = listOf(
            DEMO_BUBBLE_HEADS,
            DEMO_PAGE_FLIP
        )
    }

    object ProjectPlugins {
        private const val gradleTools = "com.android.tools.build:gradle:${Versions.gradleTools}"
        private const val timfreiheit = "de.timfreiheit.resourceplaceholders:placeholders:${Versions.timfreiheit}"
        private const val kotlinGradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinGradle}"
        private const val dcendents = "com.github.dcendents:android-maven-gradle-plugin:${Versions.dcendents}"
        private const val jfrogBintray = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.jfrogBintray}"

        val allProjectLevelPlugins = listOf(
            gradleTools, timfreiheit, kotlinGradle, dcendents, jfrogBintray
        )
    }

    object AppPlugins {
        //id
        const val application = "com.android.application"
        const val library = "com.android.library"
        const val timfreiheit = "de.timfreiheit.resourceplaceholders"

        //kotlin
        const val android = "android"
        const val androidExtensions = "android.extensions"
        const val kapt = "kapt"
    }

    object Repositories {
        const val gradle = "https://plugins.gradle.org/m2"
        const val jitpack = "https://jitpack.io"

    }

    object Versions {
        const val compileSdkVersion = 30
        const val targetSdkVersion = 30
        const val minSdkVersion = 23
        const val buildToolsVersion = "30.0.1"

        const val kotlinGradle = "1.3.72"
        const val gradleTools = "4.0.1"
        const val timfreiheit = "0.3"
        const val dcendents = "2.1"
        const val jfrogBintray = "1.8.4"
    }
}