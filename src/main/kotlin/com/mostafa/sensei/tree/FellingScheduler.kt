package com.mostafa.sensei.tree

import com.mostafa.sensei.config.TreeConfig
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags

/** Owns active jobs and distributes a fixed amount of work across server ticks. */
internal object FellingScheduler {
    private const val TASKS_PER_TICK = 8
    private const val MAX_QUEUED_TREES = 64
    private val detector = TreeDetector(NaturalTreeProtection)
    private val tasks = ArrayDeque<FellingTask>()
    private val reserved = mutableMapOf<ServerLevel, MutableSet<BlockPos>>()

    fun register() {
        PlayerBlockBreakEvents.AFTER.register { level, player, pos, state, _ ->
            val world = level as? ServerLevel ?: return@register
            if (tasks.size >= MAX_QUEUED_TREES) return@register
            val settings = TreeConfig.current
            if (player.isShiftKeyDown != settings.requireShift ||
                !state.`is`(BlockTags.LOGS) { true } ||
                !player.mainHandItem.`is` { it.`is`(ItemTags.AXES) }
            ) return@register

            val occupied = reserved[world]
            if (occupied?.contains(pos) == true) return@register
            val logs = detector.findTree(world, pos, state.block, settings.maxLogs) ?: return@register
            if (logs.any { occupied?.contains(it) == true }) return@register
            reserved.getOrPut(world) { mutableSetOf() }.addAll(logs)
            tasks.addLast(FellingTask(world, player, pos.immutable(), state.block, player.mainHandItem, logs))
        }

        ServerTickEvents.END_SERVER_TICK.register {
            repeat(minOf(TASKS_PER_TICK, tasks.size)) {
                val task = tasks.removeFirst()
                if (task.tick()) {
                    tasks.addLast(task)
                } else {
                    val occupied = reserved[task.world] ?: return@repeat
                    for (pos in task.logs) occupied.remove(pos)
                    if (occupied.isEmpty()) reserved.remove(task.world)
                }
            }
        }
        ServerLifecycleEvents.SERVER_STOPPED.register {
            tasks.clear()
            reserved.clear()
        }
    }
}
