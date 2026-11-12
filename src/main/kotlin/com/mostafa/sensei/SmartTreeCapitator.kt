package com.mostafa.sensei

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

object SmartTreeCapitator : ModInitializer {
    const val MOD_ID: String = "smart-tree-capitator"
    const val MAX_WOOD_LOGS = 64

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player, pos, state, _ ->
            if (world.isClientSide) return@register

            val heldItem = player.mainHandItem
            if (!state.`is`(BlockTags.LOGS) { true } ||
                !heldItem.`is` { it.`is`(ItemTags.AXES) }
            ) return@register

            fellTree(world, player, pos, state.block, heldItem)
        }
    }

    private fun fellTree(
        world: Level,
        player: Player,
        startPos: BlockPos,
        log: Block,
        axe: ItemStack
    ) {
        val visited = hashSetOf(startPos)
        val queue = ArrayDeque<BlockPos>()
        queue.addLast(startPos)
        var brokenLogs = 0

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            for (neighbor in current.upperNeighbors()) {
                if (!visited.add(neighbor)) continue
                if (world.getBlockState(neighbor).block != log) continue
                if (!player.isCreative && axe.isDamageableItem && axe.nextDamageWillBreak()) return

                if (world.destroyBlock(neighbor, true, player, 512)) {
                    brokenLogs++
                    queue.addLast(neighbor)
                    if (!player.isCreative) axe.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
                    if (brokenLogs >= MAX_WOOD_LOGS) return
                }
            }
        }
    }

    private fun BlockPos.upperNeighbors(): Sequence<BlockPos> =
        (-1..1).asSequence().flatMap { dx ->
            (0..1).asSequence().flatMap { dy ->
                (-1..1).asSequence().map { dz -> offset(dx, dy, dz) }
            }
        }.filter { it != this }

    fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
