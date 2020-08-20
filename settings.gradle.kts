import Settings_gradle.Constants.Apps.enabledModules
import Settings_gradle.Constants.CustomLibs.commons
import Settings_gradle.Constants.CustomLibs.enabledCustomLibs
import Settings_gradle.Constants.CustomLibs.dbExplorer
import Settings_gradle.Constants.buildFileName
import Settings_gradle.Constants.customLibsPath
import Settings_gradle.Constants.customViewsPath
import Settings_gradle.Constants.demoAppsPath
import Settings_gradle.Constants.enableMasterApp
import Settings_gradle.Constants.pathSeparator
import Settings_gradle.Constants.projectName

rootProject.name = projectName
rootProject.buildFileName = buildFileName

enableMasterApp = true
includeEnabledCustomLibs()
includeEnabledApps()

fun includeEnabledApps() {
    if (enableMasterApp) {
        include(":app")
    } else { // Enable Demo(s)
        enabledModules.values.filter { it.isNotEmpty() }.forEach {
            include(it)
            project(it).projectDir = File(rootDir, "$demoAppsPath$pathSeparator${it.substring(1)}")
        }
    }

}

fun includeEnabledCustomLibs() {
    enabledCustomLibs.filter { it.isNotEmpty() }.forEach {
        include(it)
        project(it).projectDir = File(rootDir, "$customLibsPath$pathSeparator${it.substring(1)}")
    }

    enabledModules.keys.filter { it.isNotEmpty() && !enabledCustomLibs.contains(it) }.forEach {
        include(it)
        val path = if(it.endsWith("View")) customViewsPath else customLibsPath
        project(it).projectDir = File(rootDir, "$path$pathSeparator${it.substring(1)}")
    }
}

private object Constants {
    var enableMasterApp         = false
    const val projectName       = "MemoryBank"
    const val buildFileName     = "build.gradle.kts"
    const val customLibsPath    = "customlibs"
    const val customViewsPath   = "customviews"
    const val demoAppsPath      = "demos"
    const val pathSeparator     = "/"

    object CustomLibs {
        const val commons       = ":Commons"
        const val dbExplorer    = ":RoomDbExplorer"

        /**
         * Should be same as {@path buildSrc/src/main/kotlin/Dependencies.kt -> CustomLibs.enabledCustomLibs}
         * */
        val enabledCustomLibs: List<String> = listOf(
            commons, dbExplorer
        )
    }

    object Apps {
        /**
         * Should be same as {@path buildSrc/src/main/kotlin/Dependencies.kt -> CustomViews.enabledCustomViews}
         * */
        val enabledModules by lazy { // it's a map of CustomView and its corresponding Demo App
            if (enableMasterApp) {
                /** here all modules (Libs and Views) will be enabled,
                no need to modify this if block all the time,
                just add new row whenever a new module is created in the project
                 **/
                mapOf(
                    //Libs
                    commons                 to "", // No Demo
                    dbExplorer          to ":RoomDbExplorerDemo",

                    //Views
                    ":AutoLinkTextView"     to ":AutoLinkTextViewDemo",
                    ":BubbleHeadsView"      to ":BubbleHeadsDemo",
                    ":CalculatorDialogView" to ":CalculatorDialogDemo",
                    ":DraggableTreeView"    to ":DraggableTreeDemo",
                    ":StickyTimelineView"   to ":StickyTimelineDemo", //Need :Commons
                    ":PageFlipView"         to ":PageFlipDemo"
                )
            } else { // here only below selected modules will be enabled
                mapOf(
                    dbExplorer          to ":RoomDbExplorerDemo"
                )
            }
        }
    }
}
