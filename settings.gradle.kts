import Settings_gradle.Constants.allCustomViews
import Settings_gradle.Constants.allDemoApps
import Settings_gradle.Constants.app
import Settings_gradle.Constants.customViewsPath
import Settings_gradle.Constants.demoAppsPath
import Settings_gradle.Constants.pathSeparator

include(app)
rootProject.name = "MemoryBank"
rootProject.buildFileName = "build.gradle.kts"

allCustomViews.forEach { // replace allCustomViews with CustomView.allAvailable
    include(it)
    project(it).projectDir = File(rootDir, "$customViewsPath$pathSeparator${it.substring(1)}")
}

allDemoApps.forEach { // replace allDemoApps with AppConfig.Apps.allDemoApps
    include(it)
    project(it).projectDir = File(rootDir, "$demoAppsPath$pathSeparator${it.substring(1)}")
}

private object Constants {
    const val app = ":app"
    const val demoAppsPath = "demos"
    const val customViewsPath = "customviews"
    const val pathSeparator = "/"

    val allCustomViews =  listOf(
        ":BubbleHeadsView",
        ":PageFlipView"
    )

    val allDemoApps = listOf(
        ":BubbleHeadsDemo",
        ":PageFlipDemo"
    )
}
