package com.mostafa.sensei

import com.mostafa.sensei.config.TreeConfig
import com.mostafa.sensei.tree.FellingScheduler
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier

object SmartTreeCapitator : ModInitializer {
    const val MOD_ID = "smart-tree-capitator"

    override fun onInitialize() {
        TreeConfig.load()
        FellingScheduler.register()
    }

    fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
