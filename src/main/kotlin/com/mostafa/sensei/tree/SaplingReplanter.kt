package com.mostafa.sensei.tree

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

internal object SaplingReplanter {
    fun replant(world: ServerLevel, player: Player, start: BlockPos, log: Block, drops: DropCollector) {
        val sapling = saplingFor(log) ?: return
        if (!world.getBlockState(start).isAir ||
            !world.getBlockState(start.below()).`is`(BlockTags.DIRT) { true } ||
            !sapling.defaultBlockState().canSurvive(world, start)
        ) return

        val fromDrops = drops.has(sapling.asItem())
        val inventory = player.inventory
        val slot = if (fromDrops || player.isCreative) null else
            (0 until inventory.containerSize).firstOrNull { inventory.getItem(it).`is`(sapling.asItem()) }
        if (!fromDrops && !player.isCreative && slot == null) return
        if (!world.setBlockAndUpdate(start, sapling.defaultBlockState())) return
        if (fromDrops) drops.takeOne(sapling.asItem())
        else if (slot != null) inventory.getItem(slot).shrink(1)
    }

    private fun saplingFor(log: Block): Block? = when (log) {
        Blocks.OAK_LOG -> Blocks.OAK_SAPLING
        Blocks.SPRUCE_LOG -> Blocks.SPRUCE_SAPLING
        Blocks.BIRCH_LOG -> Blocks.BIRCH_SAPLING
        Blocks.JUNGLE_LOG -> Blocks.JUNGLE_SAPLING
        Blocks.ACACIA_LOG -> Blocks.ACACIA_SAPLING
        Blocks.DARK_OAK_LOG -> Blocks.DARK_OAK_SAPLING
        Blocks.CHERRY_LOG -> Blocks.CHERRY_SAPLING
        Blocks.PALE_OAK_LOG -> Blocks.PALE_OAK_SAPLING
        Blocks.MANGROVE_LOG -> Blocks.MANGROVE_PROPAGULE
        else -> null
    }
}
