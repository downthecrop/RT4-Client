package GroundItems

import plugin.Plugin
import plugin.annotations.PluginMeta
import plugin.api.*
import rt4.*
import java.awt.Color


@PluginMeta(
    author = "Chisato",
    description = "Ground Items Overlay",
    version = 0.1
)
open class plugin : Plugin() {
    override fun Draw() {
        renderGroundItemNames()
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
                        API.calculateScreenPosition((x shl 7) + 64, 64, (y shl 7) + 64)
                        API.DrawRect(ScriptRunner.spriteDrawX-32, ScriptRunner.spriteDrawY-32, 64, 64, 16711867)
                        API.DrawText(
                            FontType.SMALL,
                            FontColor.fromColor(Color.WHITE),
                            TextModifier.CENTER,
                            itemDef.name.toString(),
                            ScriptRunner.spriteDrawX,
                            ScriptRunner.spriteDrawY-offset
                        )
                        offset += 12
                        item = objstacknodeLL.next() as ObjStackNode?
                    }
                }
            }
        }
    }
}

