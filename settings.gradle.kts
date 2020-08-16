import Settings_gradle.Constants.Apps.enabledCustomViews
import Settings_gradle.Constants.CustomLibs.enabledCustomLibs
import Settings_gradle.Constants.buildFileName
import Settings_gradle.Constants.customLibsPath
import Settings_gradle.Constants.customViewsPath
import Settings_gradle.Constants.demoAppsPath
import Settings_gradle.Constants.enableMasterApp
import Settings_gradle.Constants.pathSeparator
import Settings_gradle.Constants.projectName

//includeEnabledCustomLibs()
includeEnabledApps(enableMasterApp)

rootProject.name = projectName
rootProject.buildFileName = buildFileName

fun includeEnabledApps(isApp: Boolean) {
    enabledCustomViews.keys.filter { it.isNotEmpty() }.forEach {
        include(it)
        project(it).projectDir = File(rootDir, "$customViewsPath$pathSeparator${it.substring(1)}")
    }
    if (isApp) {
        include(":app")
    } else {
        enabledCustomViews.values.filter { it.isNotEmpty() }.forEach {
            include(it)
            project(it).projectDir = File(rootDir, "$demoAppsPath$pathSeparator${it.substring(1)}")
        }
    }
}

fun includeEnabledCustomLibs() {
    enabledCustomLibs.forEach { // replace allCustomViews with CustomView.allAvailable
        include(it)
        project(it).projectDir = File(rootDir, "$customLibsPath$pathSeparator${it.substring(1)}")
    }
}

private object Constants {
    var enableMasterApp = false
    const val projectName = "MemoryBank"
    const val buildFileName = "build.gradle.kts"
    const val customLibsPath = "customlibs"
    const val customViewsPath = "customviews"
    const val demoAppsPath = "demos"
    const val pathSeparator = "/"

    object CustomLibs {
        const val commons = ":Commons"

        /**
         * Should be same as {@path buildSrc/src/main/kotlin/Dependencies.kt -> CustomLibs.enabledCustomLibs}
         * */
        val enabledCustomLibs: List<String> = listOf(
            commons
        )
    }

    object Apps {
        /**
         * Should be same as {@path buildSrc/src/main/kotlin/Dependencies.kt -> CustomViews.enabledCustomViews}
         * */
        val enabledCustomViews by lazy { // it's a map of CustomView and its corresponding Demo App
            if (enableMasterApp) {
                /** here all CustomViews will be enabled,
                no need to modify this if block all the time,
                just add new row when a new module is created in the project
                 **/
                mapOf(
                    ":BubbleHeadsView"      to ":BubbleHeadsDemo",
                    ":CalculatorDialogView" to ":CalculatorDialogDemo",
                    ":DraggableTreeView"    to ":DraggableTreeDemo",
                    ":StickyTimelineView"   to ":StickyTimelineDemo", //Need :Commons
                    ":PageFlipView"         to ":PageFlipDemo"
                )
            } else { // here only below selected customViews will be enabled
                mapOf(
                    ":PageFlipView"         to ":PageFlipDemo"
                )
            }
        }
    }
}
