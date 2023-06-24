package GroundItems

import plugin.Plugin
import plugin.annotations.PluginMeta
import plugin.api.API
import plugin.api.FontColor
import plugin.api.FontType
import plugin.api.TextModifier
import rt4.*
import java.awt.Color
import java.text.DecimalFormat


@PluginMeta(
    author = "Chisato",
    description = "Ground Items Overlay",
    version = 0.1
)
open class plugin : Plugin() {

    // Define keys for each threshold
    private var lowValue = 0
    private var mediumValue = 0
    private var highValue = 0
    private var insaneValue = 0
    private var hideBelowValue = 0

    val formatter = DecimalFormat("#,###")
    override fun Init() {
        // Load the stored thresholds, with some default values if not found
        lowValue = API.GetData("low-value") as? Int ?: 20000
        mediumValue = API.GetData("medium-value") as? Int ?: 100000
        highValue = API.GetData("high-value") as? Int ?: 1000000
        insaneValue = API.GetData("insane-value") as? Int ?: 10000000
        hideBelowValue = API.GetData("hide-below-value") as? Int ?: 0
    }


    // Define the color codes in a map for easy reference
    val colorMap = mapOf(
        "hidden" to "#808080", // GRAY color in hex
        "lowValue" to "#66B2FF", // Light Blue
        "mediumValue" to "#99FF99", // Light Green
        "highValue" to "#FF9600", // Orange
        "insaneValue" to "#FF66B2" // Pink
    )

    override fun Draw() {
        renderGroundItemNames()
    }

    override fun ProcessCommand(commandStr: String, args: Array<out String>?) {
        println(args.toString());
        when (commandStr.toLowerCase()) {
            "::setlow" -> {
                args ?: return
                val valueArg = args[0].toInt()
                if (valueArg >= 0) {
                    API.StoreData("low-value", valueArg)
                }
            }

            "::setmed" -> {
                args ?: return
                val valueArg = args[0].toInt()
                if (valueArg > 0) {
                    API.StoreData("medium-value", valueArg)
                }
            }

            "::sethigh" -> {
                args ?: return
                val valueArg = args[0].toInt()
                if (valueArg > 0) {
                    API.StoreData("high-value", valueArg)
                }
            }

            "::setinsane" -> {
                args ?: return
                val valueArg = args[0].toInt()
                if (valueArg > 0) {
                    API.StoreData("insane-value", valueArg)
                }
            }

            "::sethide" -> {
                args ?: return
                val valueArg = args[0].toInt()
                if (valueArg >= 0) {
                    API.StoreData("hide-below-value", valueArg)
                }
            }
            "::resetranges" -> {
                resetRanges()
            }
        }
        Init();
    }

    open fun renderGroundItemNames() {
        /*
        calcEnenityScreenPos
        arg0: EastWest
        arg1: Y Pos in world space
        arg2: NorthSouth
         */

        for (x in 0..103) {
            for (y in 0..103) {
                val objstacknodeLL = SceneGraph.objStacks[Player.plane][x][y]
                var offset = 8
                if (objstacknodeLL != null) {
                    var item = objstacknodeLL.head() as ObjStackNode?
                    while (item != null) {
                        val itemDef = ObjTypeList.get(item.value.type)

                        // Calculate the HA value
                        val haValue = Math.round(itemDef.cost * 0.6).toInt()

                        // If the HA value is below the hide threshold, don't render
                        if (haValue < hideBelowValue) {
                            item = objstacknodeLL.next() as ObjStackNode?
                            continue
                        }

                        API.calculateScreenPosition((x shl 7) + 64, 64, (y shl 7) + 64)

                        // Determine the color based on the HA value
                        val haColor = when {
                            haValue < lowValue -> colorMap["lowValue"]
                            haValue < mediumValue -> colorMap["mediumValue"]
                            haValue < highValue -> colorMap["highValue"]
                            else -> colorMap["insaneValue"]
                        } ?: "#FFFFFF"  // default to white if not found in the map

                        // Convert the hex color to an int
                        val colorInt = haColor.drop(1).toInt(16)
                        val formattedHaValue = formatter.format(haValue)

                        // Apply the color to the text
                        API.DrawText(
                            FontType.LARGE,
                            FontColor.fromColor(Color(colorInt)),
                            TextModifier.CENTER,
                            "${itemDef.name} (HA: $formattedHaValue gp)",
                            ScriptRunner.spriteDrawX,
                            ScriptRunner.spriteDrawY - offset
                        )
                        offset += 12
                        item = objstacknodeLL.next() as ObjStackNode?
                    }
                }
            }
        }
    }

    open fun resetRanges() {
        lowValue = 20000
        mediumValue = 100000
        highValue = 1000000
        insaneValue = 10000000
        hideBelowValue = 0
        API.StoreData("low-value", lowValue)
        API.StoreData("medium-value", mediumValue)
        API.StoreData("high-value", highValue)
        API.StoreData("insane-value", insaneValue)
        API.StoreData("hide-below-value", hideBelowValue)
    }
}