import Settings_gradle.Constants.Apps.allCustomLibs
import Settings_gradle.Constants.Apps.allCustomViews
import Settings_gradle.Constants.Apps.allModules
import Settings_gradle.Constants.Apps.slidingCardsDemo
import Settings_gradle.Constants.Apps.stickyTimelineDemo
import Settings_gradle.Constants.app
import Settings_gradle.Constants.buildFileName
import Settings_gradle.Constants.customLibsPath
import Settings_gradle.Constants.customViewsPath
import Settings_gradle.Constants.demoAppsPath
import Settings_gradle.Constants.pathSeparator
import Settings_gradle.Constants.projectName

rootProject.name = projectName
rootProject.buildFileName = buildFileName

val enabledModules = setOf(
    slidingCardsDemo
)

private object Constants {
    const val app = ":app"
    const val projectName = "MemoryBank"
    const val buildFileName = "build.gradle.kts"
    const val customLibsPath = "customlibs"
    const val customViewsPath = "customviews"
    const val demoAppsPath = "demos"
    const val pathSeparator = "/"

    object Apps {
        //CustomLibs
        const val commons = ":Commons"
        const val dbExplorer = ":RoomDbExplorer"

        /**
         * Should be same as [CustomLibs.enabledCustomLibs]
         * */
        val allCustomLibs = listOf(
            commons,
            dbExplorer
        )

        //CustomViews
        const val autoLink = ":AutoLinkTextView"
        const val bubbleHeads = ":BubbleHeadsView"
        const val calcDialog = ":CalculatorDialogView"
        const val draggableTree = ":DraggableTreeView"
        const val pageFlip = ":PageFlipView"
        const val slidingCards = ":SlidingCardsView"
        const val stickyTimeline = ":StickyTimelineView"

        /**
         * Should be same as [CustomViews.enabledCustomViews]
         **/
        val allCustomViews = listOf(
            autoLink,
            bubbleHeads,
            draggableTree,
            calcDialog,
            pageFlip,
            slidingCards,
            stickyTimeline
        )

        //DemoApps
        const val dbExplorerDemo = ":RoomDbExplorerDemo"

        const val autoLinkDemo = ":AutoLinkTextViewDemo"
        const val bubbleHeadsDemo = ":BubbleHeadsDemo"
        const val calcDialogDemo = ":CalculatorDialogDemo"
        const val draggableTreeDemo = ":DraggableTreeDemo"
        const val pageFlipDemo = ":PageFlipDemo"
        const val slidingCardsDemo = ":SlidingCardsDemo"
        const val stickyTimelineDemo = ":StickyTimelineDemo"

        val allModules = mapOf(
            //MasterApp
            app to CustomModule(app, setOf(commons, dbExplorer)),

            //CustomLibs
            commons to CustomModule(commons),
            dbExplorer to CustomModule(dbExplorer),

            //CustomViews
            autoLink to CustomModule(autoLink),
            bubbleHeads to CustomModule(bubbleHeads),
            calcDialog to CustomModule(calcDialog),
            draggableTree to CustomModule(draggableTree),
            pageFlip to CustomModule(pageFlip),
            slidingCards to CustomModule(slidingCards, setOf(commons)),
            stickyTimeline to CustomModule(stickyTimeline, setOf(commons)),

            //DemoApps
            dbExplorerDemo to CustomModule(dbExplorerDemo, setOf(dbExplorer)),

            autoLinkDemo to CustomModule(autoLinkDemo, setOf(autoLink)),
            bubbleHeadsDemo to CustomModule(bubbleHeadsDemo, setOf(bubbleHeads)),
            calcDialogDemo to CustomModule(calcDialogDemo, setOf(calcDialog)),
            draggableTreeDemo to CustomModule(draggableTreeDemo, setOf(draggableTree)),
            pageFlipDemo to CustomModule(pageFlipDemo, setOf(pageFlip)),
            slidingCardsDemo to CustomModule(slidingCardsDemo, setOf(slidingCards)),
            stickyTimelineDemo to CustomModule(stickyTimelineDemo, setOf(stickyTimeline))
        )
    }
}

includeEnabledApps()

fun includeEnabledApps() {
    enabledModules.filter{ it.isNotEmpty() }.forEach {
        includeModule(allModules[it])
    }
}

fun includeModule(module: CustomModule?) {
    module?.apply {
        dependencies.filter{ it.isNotEmpty() }.forEach {
            includeModule(allModules[it])
        }
        if (isEnabled && name.isNotEmpty()) {
            include(name)
            if (path.isNotEmpty()) {
                project(name).projectDir = File(rootDir, "$path$pathSeparator${name.substring(1)}")
            }
            allModules[name]?.isEnabled = false
        }
    }
}

data class CustomModule(
    val name: String,
    val dependencies: Set<String> = setOf(""),
    var isEnabled: Boolean = true
) {
    val path by lazy {
        when {
            app == name -> ""
            allCustomLibs.contains(name) -> customLibsPath
            allCustomViews.contains(name) -> customViewsPath
            else -> demoAppsPath
        }
    }
}

