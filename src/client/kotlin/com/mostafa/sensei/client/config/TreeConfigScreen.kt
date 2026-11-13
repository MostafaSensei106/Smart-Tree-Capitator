package com.mostafa.sensei.client.config

import com.mostafa.sensei.config.TreeConfig
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.minecraft.network.chat.Component

class TreeConfigScreen : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory { parent ->
        val settings = TreeConfig.current
        var maxLogs = settings.maxLogs
        var requireShift = settings.requireShift
        val limit = Option.createBuilder<Int>()
            .name(Component.literal("Maximum logs"))
            .binding(64, { maxLogs }, { maxLogs = it })
            .controller { IntegerSliderControllerBuilder.create(it).range(1, 512).step(1) }
            .build()
        val shift = Option.createBuilder<Boolean>()
            .name(Component.literal("Require Shift to fell trees"))
            .binding(false, { requireShift }, { requireShift = it })
            .controller { TickBoxControllerBuilder.create(it) }
            .build()
        YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Smart Tree Capitator"))
            .category(ConfigCategory.createBuilder().name(Component.literal("Tree felling"))
                .option(limit).option(shift).build())
            .save { TreeConfig.update(maxLogs, requireShift) }
            .build().generateScreen(parent)
    }
}
