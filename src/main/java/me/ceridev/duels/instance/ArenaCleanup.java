package me.ceridev.duels.instance;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCleanup extends BukkitRunnable {

    private DuelPlugin plugin;
    private Arena arena;
    private int cleanupSeconds;

    public ArenaCleanup(DuelPlugin plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.cleanupSeconds = 20;
    }

    public void start() {
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        if (cleanupSeconds == 0) {
            arena.reset(true);
        }
        if (cleanupSeconds == 20) {
            arena.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn in 20 seconds...");
        } else if (cleanupSeconds == 10) {
            arena.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn in 10 seconds...");
        } else if (cleanupSeconds <= 3) {
            arena.sendMessage(ChatColor.YELLOW + "Teleporting in " + cleanupSeconds);
        }

        cleanupSeconds--;
    }
}
