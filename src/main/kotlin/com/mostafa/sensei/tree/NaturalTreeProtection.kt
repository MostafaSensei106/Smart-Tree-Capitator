package com.mostafa.sensei.tree

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.BedBlock
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.IronBarsBlock
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.StainedGlassBlock
import net.minecraft.world.level.block.StainedGlassPaneBlock
import net.minecraft.world.level.block.TrapDoorBlock
import net.minecraft.world.level.block.state.BlockState

internal fun interface TreeProtectionPolicy {
    fun accepts(world: ServerLevel, logs: List<BlockPos>, stateAt: (BlockPos) -> BlockState): Boolean
}

/** Requires natural crown leaves and rejects common building blocks beside any log. */
internal object NaturalTreeProtection : TreeProtectionPolicy {
    override fun accepts(world: ServerLevel, logs: List<BlockPos>, stateAt: (BlockPos) -> BlockState): Boolean {
        val logPositions = logs.toHashSet()
        val top = logs.maxOf { it.y }
        var hasNaturalLeaves = false

        for (pos in logs) {
            for (dx in -1..1) for (dy in -1..1) for (dz in -1..1) {
                if (dx == 0 && dy == 0 && dz == 0) continue
                val adjacent = pos.offset(dx, dy, dz)
                if (adjacent in logPositions) continue
                if (!world.isLoaded(adjacent)) return false
                val state = stateAt(adjacent)
                if (isBuildingBlock(state)) return false
                if (pos.y >= top - 2 && state.block is LeavesBlock &&
                    !state.getValue(LeavesBlock.PERSISTENT)
                ) hasNaturalLeaves = true
            }
        }
        return hasNaturalLeaves
    }

    private fun isBuildingBlock(state: BlockState): Boolean =
        state.block is DoorBlock || state.block is TrapDoorBlock || state.block is BedBlock ||
            state.block == Blocks.GLASS || state.block == Blocks.GLASS_PANE ||
            state.block == Blocks.TINTED_GLASS || state.block is StainedGlassBlock ||
            state.block is StainedGlassPaneBlock || state.block is IronBarsBlock ||
            state.`is`(BlockTags.DOORS) { true } || state.`is`(BlockTags.BEDS) { true } ||
            state.`is`(BlockTags.PLANKS) { true } || state.`is`(BlockTags.STAIRS) { true } ||
            state.`is`(BlockTags.SLABS) { true } || state.`is`(BlockTags.FENCES) { true }
}
