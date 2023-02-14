package me.ceridev.duels.config;

import jdk.nashorn.internal.objects.annotations.Getter;
import me.ceridev.duels.instance.DuelPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class Database {

    private final DuelPlugin plugin;

    private File databaseFile;
    private FileConfiguration database;

    public Database(DuelPlugin plugin) {
        this.plugin = plugin;
    }
    public void setup() {
        databaseFile = new File(plugin.getDataFolder(), "config.yml");
        if (!databaseFile.exists()) {
            plugin.saveDefaultConfig();
        }
        database = plugin.getConfig();
    }

    @Getter
    public String getURI() { return database.getString("uri"); }

    @Getter
    public Location getGlobalSpawnLocation() {
        return new Location(
                Bukkit.getWorld(database.getString("global_spawn.world")),
                database.getInt("global_spawn.x"),
                database.getInt("global_spawn.y"),
                database.getInt("global_spawn.z"),
                (float) database.getInt("global_spawn.yaw"),
                (float) database.getInt("global_spawn.pitch")
                );
    }

}