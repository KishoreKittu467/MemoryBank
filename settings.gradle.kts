import Settings_gradle.Constants.allCustomLibs
import Settings_gradle.Constants.allCustomViews
import Settings_gradle.Constants.Apps.allDemoApps
import Settings_gradle.Constants.Apps.app
import Settings_gradle.Constants.customLibsPath
import Settings_gradle.Constants.customViewsPath
import Settings_gradle.Constants.demoAppsPath
import Settings_gradle.Constants.pathSeparator

include(app)
rootProject.name = "MemoryBank"
rootProject.buildFileName = "build.gradle.kts"

allCustomLibs.forEach { // replace allCustomViews with CustomView.allAvailable
    include(it)
    project(it).projectDir = File(rootDir, "$customLibsPath$pathSeparator${it.substring(1)}")
}

allCustomViews.forEach { // replace allCustomViews with CustomView.allAvailable
    include(it)
    project(it).projectDir = File(rootDir, "$customViewsPath$pathSeparator${it.substring(1)}")
}

allDemoApps.forEach { // replace allDemoApps with AppConfig.Apps.allDemoApps
    include(it)
    project(it).projectDir = File(rootDir, "$demoAppsPath$pathSeparator${it.substring(1)}")
}

private object Constants {
    const val demoAppsPath = "demos"
    const val customLibsPath = "customlibs"
    const val customViewsPath = "customviews"
    const val pathSeparator = "/"

    object Apps {
        const val app = ":app"
        const val demoBubbleHeads = ":BubbleHeadsDemo"
        const val demoCalcDialog = ":CalculatorDialogDemo"
        const val demoPageFlip = ":PageFlipDemo"

        val allDemoApps = listOf(
            demoBubbleHeads,
            demoCalcDialog,
            demoPageFlip
        )
    }

    val allCustomLibs = listOf(
        ":Commons"
    )

    val allCustomViews =  listOf(
        ":BubbleHeadsView",
        ":CalculatorDialogView",
        ":PageFlipView"
    )
}
