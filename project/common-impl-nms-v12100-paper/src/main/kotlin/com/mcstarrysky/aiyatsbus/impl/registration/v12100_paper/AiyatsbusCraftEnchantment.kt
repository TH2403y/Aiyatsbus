package com.mcstarrysky.aiyatsbus.impl.registration.v12100_paper

import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantment
import com.mcstarrysky.aiyatsbus.core.AiyatsbusEnchantmentBase
import com.mcstarrysky.aiyatsbus.core.util.legacyToAdventure
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.craftbukkit.enchantments.CraftEnchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

/**
 * Aiyatsbus
 * com.mcstarrysky.aiyatsbus.impl.registration.modern.AiyatsbusCraftEnchantment
 *
 * @author mical
 * @since 2024/2/17 17:15
 */
class AiyatsbusCraftEnchantment(
    private val enchant: AiyatsbusEnchantmentBase,
    nmsEnchantment: Enchantment
) : CraftEnchantment(enchant.enchantmentKey, nmsEnchantment), AiyatsbusEnchantment by enchant {

    init {
        enchant.enchantment = this
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return enchant.canEnchantItem(item)
    }

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        return enchant.conflictsWith(other)
    }

    override fun translationKey(): String {
        return enchant.basicData.id
    }

    override fun getName(): String {
        return enchant.basicData.id.uppercase()
    }

    override fun getMaxLevel(): Int {
        return enchant.basicData.maxLevel
    }

    override fun getStartLevel(): Int = 1

    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.ALL

    override fun isTreasure(): Boolean {
        return enchant.alternativeData.isTreasure
    }

    override fun isCursed(): Boolean {
        return enchant.alternativeData.isCursed
    }

    override fun displayName(level: Int): Component {
        return enchant.displayName(level).legacyToAdventure()
    }

    override fun isTradeable(): Boolean {
        return enchant.alternativeData.isTradeable
    }

    override fun isDiscoverable(): Boolean {
        return enchant.alternativeData.isDiscoverable
    }

    override fun getMinModifiedCost(level: Int): Int {
        return 0
    }

    override fun getMaxModifiedCost(level: Int): Int {
        return 0
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float = 0.0f

    override fun getActiveSlots(): MutableSet<EquipmentSlot> = mutableSetOf()

    override fun equals(other: Any?): Boolean {
        return other is AiyatsbusEnchantment && this.enchantmentKey == other.enchantmentKey
    }

    override fun hashCode(): Int {
        return this.enchantmentKey.hashCode()
    }

    override fun toString(): String {
        return "AiyatsbusCraftEnchantment(key=$key)"
    }

    /**
     * 2024-4-7 22:18:
     * 傻逼 Paper 你今天突然更新一个这个函数让我实现,
     * 实现了还告诉我这个方法被标记为移除了,
     * 你妈死了吧, 你他妈了逼的
     *
     * 你改你妈个🥚，我爱说实话
     */
    override fun getTranslationKey(): String {
        return "aiyatsbus:enchantments.id"
    }
}