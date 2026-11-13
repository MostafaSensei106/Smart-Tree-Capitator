package com.mostafa.sensei.tree

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

/** Finds matching connected logs once, without loading chunks or exceeding the configured limit. */
internal class TreeDetector(private val protection: TreeProtectionPolicy) {
    fun findTree(world: ServerLevel, start: BlockPos, log: Block, limit: Int): List<BlockPos>? {
        val logs = mutableListOf(start.immutable())
        val seen = hashSetOf(start.immutable())
        val observed = hashMapOf<BlockPos, BlockState>()
        val queue = ArrayDeque<BlockPos>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val pos = queue.removeFirst()
            for (dx in -1..1) for (dy in 0..1) for (dz in -1..1) {
                if (dx == 0 && dy == 0 && dz == 0) continue
                val next = pos.offset(dx, dy, dz)
                if (!seen.add(next)) continue
                if (!world.isLoaded(next)) return null
                val state = world.getBlockState(next)
                observed[next] = state
                if (state.block != log) continue
                if (logs.size >= limit) return null
                logs.add(next)
                queue.add(next)
            }
        }

        if (!protection.accepts(world, logs) { pos ->
                observed.getOrPut(pos) { world.getBlockState(pos) }
            }
        ) return null
        return logs.sortedWith(LOG_ORDER)
    }

    private companion object {
        val LOG_ORDER = compareBy<BlockPos> { it.y }.thenBy { it.x }.thenBy { it.z }
    }
}
