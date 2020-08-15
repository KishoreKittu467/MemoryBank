import org.gradle.api.JavaVersion

object ProjectPlugins {
    private const val gradleTools = "com.android.tools.build:gradle:${Versions.gradleTools}"
    private const val timfreiheit = "de.timfreiheit.resourceplaceholders:placeholders:${Versions.timfreiheit}"
    private const val dcendents = "com.github.dcendents:android-maven-gradle-plugin:${Versions.dcendents}"
    private const val jfrogBintray = "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.jfrogBintray}"

    val allProjectLevelPlugins = listOf(
        gradleTools, timfreiheit, dcendents, jfrogBintray
    )
}

object Repositories {
    const val gradle = "https://plugins.gradle.org/m2"
    const val jitpack = "https://jitpack.io"
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

object CustomLibs {
    //Do not make these private even after auto suggestion
    // TODO: 20/07/20 try to add these to settings.gradle.kts
    const val libCommons = ":Commons"

    val allCustomLibs = listOf(
        libCommons
    )
}

object CustomView {

    //Do not make these private even after auto suggestion
    // TODO: 20/07/20 try to add these to settings.gradle.kts
    const val bubbleHeads = ":BubbleHeadsView"
    const val calcDialog = ":CalculatorDialogView"
    const val pageFlip = ":PageFlipView"
    const val stickyTimeline = ":StickyTimeline"

    val allCustomViews: List<String> =
        listOf(
            bubbleHeads,
            calcDialog,
            pageFlip,
            stickyTimeline
        )
}

object Deps { // Do not make these private even after auto suggestion
    //implementation
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlinGradle}"
    const val jodaTime = "joda-time:joda-time:${Versions.JODA_TIME}"
    const val multidex = "androidx.multidex:multidex:${Versions.MULTIDEX}"
    const val coreKTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val material = "com.google.android.material:material:${Versions.MATERIAL}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.MATERIAL}"
    const val room = "androidx.room:room-runtime:${Versions.ROOM}"
    const val lifeCycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFE_CYCLE_EXT}"
    const val fragments = "androidx.fragment:fragment:${Versions.FRAGMENTS}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val docFile = "androidx.documentfile:documentfile:${Versions.DOC_FILE}"
    const val exifInterface = "androidx.exifinterface:exifinterface:${Versions.EXIF_INTERFACE}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val androignito = "com.andrognito.patternlockview:patternlockview:${Versions.ANDROGNITO}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.MATERIAL}"

    //api
    const val ajalt = "com.github.ajalt.reprint:core:${Versions.AJALT}"
    const val dulingoRTL = "com.duolingo.open:rtl-viewpager:${Versions.DULINGO_RTL}"
    const val gson = "com.google.code.gson:gson:${Versions.GSON}"

    //kapt
    const val roomCompiler = "androidx.room:room-compiler:${Versions.ROOM}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"

    //testImplementation
    const val junit = "junit:junit:${Versions.JUNIT}"

    //androidTestImplementation
    const val junitExt = "androidx.test.ext:junit:${Versions.JUNIT_EXT}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"

    //commons
    val allCommonApiDeps = listOf(
        stdlib, coreKTX, constraintLayout, androignito, docFile, swipeRefreshLayout,
        ajalt, dulingoRTL, gson, appCompat, material, glide
    )

    //App
    val allAppImplDeps: List<String> = listOf(
        jodaTime, multidex, material, room, appCompat, lifeCycleExtensions, exifInterface, glide
    )

    val allCommonKaptDeps = listOf(roomCompiler, glideCompiler)

    val allCommonTestImplDeps = listOf(junit)

    val allCommonAndroidTestImplDeps = listOf(junitExt, espresso)
}

object Versions {

    //apps
    const val compileSdkVersion = 30
    const val targetSdkVersion = 30
    const val minSdkVersion = 23
    const val buildToolsVersion = "30.0.1"
    const val targetJavaVersion = "1.8"
    val compileJavaVersion = JavaVersion.VERSION_1_8

    //project plugins
    const val gradleTools = "4.0.1"
    const val timfreiheit = "0.3"
    const val dcendents = "2.1"
    const val jfrogBintray = "1.8.4"

    //libs
    const val kotlinGradle = "1.3.72"
    const val ROOM = "2.2.5"
    const val GLIDE = "4.11.0"
    const val MATERIAL = "1.1.0"
    const val JODA_TIME = "2.10.2"
    const val MULTIDEX = "2.0.1"
    const val CORE_KTX = "1.3.0"
    const val LIFE_CYCLE_EXT = "2.2.0"
    const val FRAGMENTS = "1.3.0-alpha07"
    const val CONSTRAINT_LAYOUT = "2.0.0-rc1"
    const val DOC_FILE = "1.0.1"
    const val EXIF_INTERFACE = "1.2.0"
    const val AJALT = "3.3.0@aar"
    const val DULINGO_RTL = "1.0.3"
    const val GSON = "2.8.6"
    const val JUNIT = "4.13"
    const val JUNIT_EXT = "1.1.1"
    const val ESPRESSO = "3.2.0"
    const val ANDROGNITO = "1.0.0"
}
