package com.mostafa.sensei.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import java.nio.file.Files

data class TreeSettings(val maxLogs: Int = 64, val requireShift: Boolean = false)

object TreeConfig {
    private val path = FabricLoader.getInstance().configDir.resolve("smart-tree-capitator.json")
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val logger = LoggerFactory.getLogger("smart-tree-capitator")

    @Volatile
    private var settings = TreeSettings()

    val current: TreeSettings get() = settings

    fun load() {
        if (!Files.exists(path)) {
            save(settings)
            return
        }
        settings = try {
            val json = JsonParser.parseString(Files.readString(path)).asJsonObject
            TreeSettings(
                maxLogs = json.get("maxLogs")?.asInt?.coerceIn(1, 512) ?: 64,
                requireShift = json.get("requireShift")?.asBoolean ?: false
            )
        } catch (error: Exception) {
            logger.warn("Could not read {}; using default tree settings", path, error)
            TreeSettings()
        }
    }

    fun update(maxLogs: Int, requireShift: Boolean) {
        val next = TreeSettings(maxLogs.coerceIn(1, 512), requireShift)
        save(next)
        settings = next
    }

    private fun save(value: TreeSettings) {
        Files.createDirectories(path.parent)
        Files.writeString(path, gson.toJson(value))
    }
}
