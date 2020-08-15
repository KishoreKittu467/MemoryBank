import Settings_gradle.Constants.Apps.enabledDemoApps
import Settings_gradle.Constants.CustomLibs.enabledCustomLibs
import Settings_gradle.Constants.CustomViews.enabledCustomViews
import Settings_gradle.Constants.customLibsPath
import Settings_gradle.Constants.customViewsPath
import Settings_gradle.Constants.demoAppsPath
import Settings_gradle.Constants.pathSeparator

//include(":app")
runEnabledDemos()

rootProject.name = "MemoryBank"
rootProject.buildFileName = "build.gradle.kts"

enabledCustomLibs.forEach { // replace allCustomViews with CustomView.allAvailable
    include(it)
    project(it).projectDir = File(rootDir, "$customLibsPath$pathSeparator${it.substring(1)}")
}

enabledCustomViews.forEach { // replace allCustomViews with CustomView.allAvailable
    include(it)
    project(it).projectDir = File(rootDir, "$customViewsPath$pathSeparator${it.substring(1)}")
}

fun runEnabledDemos() {
    enabledDemoApps.forEach {
        include(it)
        project(it).projectDir = File(rootDir, "$demoAppsPath$pathSeparator${it.substring(1)}")
    }
}

private object Constants {
    const val demoAppsPath = "demos"
    const val customLibsPath = "customlibs"
    const val customViewsPath = "customviews"
    const val pathSeparator = "/"

    object CustomLibs {
        const val commons = ":Commons"

        val enabledCustomLibs = listOf(
            commons
        )
    }

    object CustomViews {
        const val bubbleHeads = ":BubbleHeadsView"
        const val calcDialog = ":CalculatorDialogView"
        const val pageFlip = ":PageFlipView"
        const val stickyTimeline = ":StickyTimeline"

        val enabledCustomViews =  listOf(
            stickyTimeline
        )
    }

    object Apps {
        const val app = ":app"
        const val demoBubbleHeads = ":BubbleHeadsDemo"
        const val demoCalcDialog = ":CalculatorDialogDemo"
        const val demoPageFlip = ":PageFlipDemo"
        const val demoStickyTimeline = ":StickyTimelineDemo"

        val enabledDemoApps = listOf(
            demoStickyTimeline
        )
    }
}
