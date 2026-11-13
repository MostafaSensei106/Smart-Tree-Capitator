package com.mostafa.sensei.tree

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/** Combines equivalent loot before creating item entities at the tree base. */
internal class DropCollector {
    private val stacksByItem = mutableMapOf<Item, MutableList<ItemStack>>()

    fun addAll(items: List<ItemStack>) {
        for (item in items) add(item)
    }

    private fun add(stack: ItemStack) {
        if (stack.isEmpty) return
        val bucket = stacksByItem.getOrPut(stack.item) { mutableListOf() }
        var remaining = stack.count
        for (existing in bucket) {
            if (!ItemStack.isSameItemSameComponents(existing, stack)) continue
            val room = existing.maxStackSize - existing.count
            if (room <= 0) continue
            val amount = minOf(room, remaining)
            existing.grow(amount)
            remaining -= amount
            if (remaining == 0) return
        }
        while (remaining > 0) {
            val amount = minOf(remaining, stack.maxStackSize)
            bucket.add(stack.copyWithCount(amount))
            remaining -= amount
        }
    }

    fun takeOne(item: Item): Boolean {
        val stack = stacksByItem[item]?.firstOrNull { !it.isEmpty } ?: return false
        stack.shrink(1)
        return true
    }

    fun has(item: Item): Boolean = stacksByItem[item]?.any { !it.isEmpty } == true

    fun spawnAt(world: ServerLevel, start: BlockPos) {
        for (bucket in stacksByItem.values) for (stack in bucket) if (!stack.isEmpty) {
            val entity = ItemEntity(world, start.x + 0.5, start.y + 0.2, start.z + 0.5, stack)
            entity.setDefaultPickUpDelay()
            world.addFreshEntity(entity)
        }
        stacksByItem.clear()
    }
}
