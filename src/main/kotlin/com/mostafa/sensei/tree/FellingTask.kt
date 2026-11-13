package com.mostafa.sensei.tree

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

/** One tree's state machine. The scheduler gives each task at most one step per turn. */
internal class FellingTask(
    val world: ServerLevel,
    private val player: Player,
    private val start: BlockPos,
    private val log: Block,
    private val axe: ItemStack,
    val logs: List<BlockPos>
) {
    private enum class Phase { LOGS, LEAVES, DONE }

    private val drops = DropCollector()
    private val leafDecay = LeafDecay(world)
    private var nextLog = 0
    private var phase = Phase.LOGS

    fun tick(): Boolean = when (phase) {
        Phase.LOGS -> tickLogs()
        Phase.LEAVES -> tickLeaves()
        Phase.DONE -> false
    }

    private fun tickLogs(): Boolean {
        if (player.isRemoved || !player.isAlive || player.level() !== world ||
            player.mainHandItem !== axe || axe.isEmpty
        ) return finish(replant = false)

        var processed = 0
        while (processed < LOGS_PER_TICK && nextLog < logs.size) {
            if (!canUseAxe()) break
            val pos = logs[nextLog++]
            processed++
            if (!world.isLoaded(pos)) return finish(replant = false)
            val state = world.getBlockState(pos)
            if (state.block != log) continue

            val loot = if (player.isCreative) emptyList() else
                Block.getDrops(state, world, pos, world.getBlockEntity(pos), player, axe)
            if (world.destroyBlock(pos, false, player, 512)) {
                drops.addAll(loot)
                world.levelEvent(2001, pos, Block.getId(state))
                if (!player.isCreative) axe.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)
                leafDecay.recordAround(pos)
            }
        }

        if (nextLog < logs.size && canUseAxe()) return true
        phase = Phase.LEAVES
        return true
    }

    private fun tickLeaves(): Boolean {
        if (leafDecay.tick(drops, collectDrops = !player.isCreative)) return true
        return finish(replant = nextLog >= logs.size)
    }

    private fun canUseAxe(): Boolean =
        player.isCreative || !axe.isDamageableItem || !axe.nextDamageWillBreak()

    private fun finish(replant: Boolean): Boolean {
        if (replant) SaplingReplanter.replant(world, player, start, log, drops)
        drops.spawnAt(world, start)
        phase = Phase.DONE
        return false
    }

    private companion object {
        const val LOGS_PER_TICK = 3
    }
}
