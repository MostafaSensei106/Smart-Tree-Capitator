package com.mostafa.sensei

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
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
    private const val TICKS_PER_LOG = 8
    private val pendingTrees = mutableListOf<FellingTask>()

    override fun onInitialize() {
        PlayerBlockBreakEvents.AFTER.register { world, player, pos, state, _ ->
            if (world.isClientSide || player.isCrouching) return@register

            val heldItem = player.mainHandItem
            if (!state.`is`(BlockTags.LOGS) { true } ||
                !heldItem.`is` { it.`is`(ItemTags.AXES) }
            ) return@register

            pendingTrees.add(FellingTask(world, player, pos, state.block, heldItem))
        }

        ServerTickEvents.END_SERVER_TICK.register { pendingTrees.removeAll { !it.tick() } }
    }

    private class FellingTask(
        private val world: Level,
        private val player: Player,
        startPos: BlockPos,
        private val log: Block,
        private val axe: ItemStack
    ) {
        private val visited = hashSetOf(startPos)
        private val queue = ArrayDeque<BlockPos>()
        private var brokenLogs = 0
        private var ticksUntilNext = TICKS_PER_LOG

        init {
            enqueueNeighbors(startPos)
        }

        fun tick(): Boolean {
            if (player.isRemoved || !player.isAlive || player.level() !== world ||
                player.isShiftKeyDown || player.mainHandItem !== axe || axe.isEmpty
            ) return false

            if (--ticksUntilNext > 0) return true

            while (queue.isNotEmpty()) {
                val next = queue.removeFirst()
                if (world.getBlockState(next).block != log) continue
                if (!player.isCreative && axe.isDamageableItem && axe.nextDamageWillBreak()) return false

                if (world.destroyBlock(next, true, player, 512)) {
                    brokenLogs++
                    if (!player.isCreative) axe.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
                    if (brokenLogs >= MAX_WOOD_LOGS) return false

                    enqueueNeighbors(next)
                    ticksUntilNext = TICKS_PER_LOG
                    return queue.isNotEmpty()
                }
            }

            return false
        }

        private fun enqueueNeighbors(pos: BlockPos) {
            for (dx in -1..1) {
                for (dy in 0..1) {
                    for (dz in -1..1) {
                        val neighbor = pos.offset(dx, dy, dz)
                        if (visited.add(neighbor)) queue.addLast(neighbor)
                    }
                }
            }
        }
    }

    fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
