object CustomView {

    //Do not make these private even after auto suggestion
    // TODO: 20/07/20 try to add these to settings.gradle.kts
    const val BUBBLE_HEADS = ":BubbleHeadsView"
    const val PAGE_FLIP = ":PageFlipView"

    val allCustomViews: List<String> =
        listOf(
            BUBBLE_HEADS,
            PAGE_FLIP
        )
}

object Deps { // Do not make these private even after auto suggestion
    //implementation
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${AppConfig.Versions.kotlinGradle}"
    const val jodaTime = "joda-time:joda-time:${Versions.JODA_TIME}"
    const val multidex = "androidx.multidex:multidex:${Versions.MULTIDEX}"
    const val coreKTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val material = "com.google.android.material:material:${Versions.MATERIAL}"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.MATERIAL}"
    const val room = "androidx.room:room-runtime:${Versions.ROOM}"
    const val lifeCycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFE_CYCLE_EXT}"
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


    val allAppImplDeps: List<String> = listOf(
        stdlib, jodaTime, multidex, coreKTX, material, swipeRefreshLayout, room, appCompat,
        lifeCycleExtensions, constraintLayout, docFile, exifInterface, glide, androignito
    )
    val allAppApiDeps: List<String> = listOf(ajalt, dulingoRTL, gson)
    val allAppKaptDeps: List<String> = listOf(roomCompiler, glideCompiler)
    val allAppTestImplDeps: List<String> = listOf(junit)
    val allAppAndroidTestImplDeps: List<String> = listOf(junitExt, espresso)
}

object Versions {
    const val ROOM = "2.2.5"
    const val GLIDE = "4.11.0"
    const val MATERIAL = "1.1.0"
    const val JODA_TIME = "2.10.2"
    const val MULTIDEX = "2.0.1"
    const val CORE_KTX = "1.3.0"
    const val LIFE_CYCLE_EXT = "2.2.0"
    const val CONSTRAINT_LAYOUT = "2.0.0-beta8"
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
