package com.mcstarrysky.aiyatsbus.module.ingame.mechanics

import com.mcstarrysky.aiyatsbus.core.*
import com.mcstarrysky.aiyatsbus.core.util.inject.Reloadable
import com.mcstarrysky.aiyatsbus.core.util.getI18nName
import com.mcstarrysky.aiyatsbus.core.util.inject.AwakePriority
import taboolib.common.LifeCycle
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.platform.util.giveItem
import taboolib.platform.util.onlinePlayers

object AntiIllegalItem {

    var task: PlatformExecutor.PlatformTask? = null

    @Reloadable
    @AwakePriority(LifeCycle.ENABLE, StandardPriorities.ANTI_ILLEGAL_ITEM)
    fun load() {
        task?.cancel()

        if (!AiyatsbusSettings.enableAntiIllegalItem)
            return

        task = submit(period = AiyatsbusSettings.antiIllegalItemInterval) {
            onlinePlayers.forEach { player ->
                val inv = player.inventory
                for (i in 0 until inv.size) {
                    val item = inv.getItem(i) ?: continue
                    val enchants = item.fixedEnchants.toList().toMutableList()
                    if (enchants.isEmpty()) continue
                    var j = 0
                    while (j < enchants.size) {
                        val tmp = item.clone()
                        val et = enchants[j].first
                        val level = enchants[j].second
                        tmp.removeEt(et)
                        val result = et.limitations.checkAvailable(AiyatsbusSettings.antiIllegalItemCheckList, tmp)
                        if (result.isFailure) {
                            enchants.removeAt(j)
                            player.giveItem(et.book(level))
                            item.removeEt(et)
                            player.sendLang(
                                "info-illegal_item",
                                item.getI18nName() to "item",
                                result.reason to "reason",
                                et.displayName() to "enchant"
                            )
                        }
                        j++
                    }
                }
            }
        }
    }
}