package com.mostafa.sensei.tree

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LeavesBlock

internal class LeafDecay(private val world: ServerLevel) {
    private val leaves = linkedSetOf<BlockPos>()
    private val scanned = hashSetOf<BlockPos>()
    private var ticks = 0

    fun recordAround(logPos: BlockPos) {
        for (dx in -2..2) for (dy in -2..2) for (dz in -2..2) {
            val pos = logPos.offset(dx, dy, dz)
            if (!scanned.add(pos) || !world.isLoaded(pos)) continue
            val state = world.getBlockState(pos)
            if (state.block is LeavesBlock && !state.getValue(LeavesBlock.PERSISTENT)) leaves.add(pos)
        }
    }

    fun tick(drops: DropCollector, collectDrops: Boolean): Boolean {
        ticks++
        val iterator = leaves.iterator()
        var processed = 0
        while (iterator.hasNext() && processed < LEAVES_PER_TICK) {
            val pos = iterator.next()
            iterator.remove()
            processed++
            if (!world.isLoaded(pos)) continue
            val state = world.getBlockState(pos)
            if (state.block !is LeavesBlock || state.getValue(LeavesBlock.PERSISTENT) ||
                state.getValue(LeavesBlock.DISTANCE) < LeavesBlock.DECAY_DISTANCE
            ) continue
            val loot = if (collectDrops) Block.getDrops(state, world, pos, world.getBlockEntity(pos)) else emptyList()
            if (world.destroyBlock(pos, false)) drops.addAll(loot)
        }
        return leaves.isNotEmpty() && ticks < LEAF_DECAY_TICKS
    }

    private companion object {
        const val LEAVES_PER_TICK = 32
        const val LEAF_DECAY_TICKS = 40
    }
}
