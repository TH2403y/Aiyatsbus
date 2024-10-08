@file:Suppress("DEPRECATION")

package com.mcstarrysky.aiyatsbus.module.compat.chat.display

import com.google.gson.JsonObject
import com.mcstarrysky.aiyatsbus.core.Aiyatsbus
import com.mcstarrysky.aiyatsbus.core.toDisplayMode
import com.mcstarrysky.aiyatsbus.core.util.JSON_PARSER
import com.mcstarrysky.aiyatsbus.core.util.isValidJson
import com.mcstarrysky.aiyatsbus.module.compat.chat.DisplayReplacer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.entity.Player
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Type

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.module.compat.chat.display.DisplayReplacerNBT
 *
 * @author mical
 * @since 2024/8/18 16:38
 */
object DisplayReplacerNBT : DisplayReplacer {

    private val gson = GsonComponentSerializer.gson()

    override fun apply(component: Component, player: Player): Component {
        try {
            var json = gson.serialize(component)

            // 尝试修复 Source: '' 的警告
            if (!json.isValidJson()) return component

            try {
                // 弱者做法: 二次解析, 防止 GsonComponentSerializer 把单引号解析成 \u0027
                json = Configuration.loadFromString(json, Type.FAST_JSON).saveToString()
            } catch (_: Throwable) {
                return component
            }

            val stacks = extractHoverEvents(json)

            for (stack in stacks) {
                val id = stack.get("id").asString
                val tag = stack.get("tag")?.asString ?: continue

                val item = Aiyatsbus.api().getMinecraftAPI().createItemStack(id, tag)
                val display = item.toDisplayMode(player)

                val target = display.displayName().hoverEvent(display.asHoverEvent())
                json = json.replace(
                    stack.get("tag").asString.flat(),
                    extractHoverEvents(gson.serialize(target)).first().get("tag").asString.flat()
                )
            }

            return gson.deserialize(json)
        } catch (_: Throwable) {
            return component
        }
    }

    private fun extractHoverEvents(jsonString: String): List<JsonObject> {
        val jsonObject = JSON_PARSER.parse(jsonString).asJsonObject
        val hoverEvents = mutableListOf<JsonObject>()

        findHoverEvents(jsonObject, hoverEvents)

        return hoverEvents
    }

    private fun findHoverEvents(jsonObject: JsonObject, hoverEvents: MutableList<JsonObject>) {
        for (entry in jsonObject.entrySet()) {
            val value = entry.value
            if (value.isJsonObject) {
                if (entry.key == "hoverEvent" && value.asJsonObject.has("action") && value.asJsonObject.get("action").asString == "show_item") {
                    hoverEvents.add(value.asJsonObject.get("contents").asJsonObject)
                } else {
                    findHoverEvents(value.asJsonObject, hoverEvents)
                }
            } else if (value.isJsonArray) {
                for (element in value.asJsonArray) {
                    if (element.isJsonObject) {
                        findHoverEvents(element.asJsonObject, hoverEvents)
                    }
                }
            }
        }
    }

    private fun String.flat(): String {
        return this.replace("\"", "\\\"")
    }
}