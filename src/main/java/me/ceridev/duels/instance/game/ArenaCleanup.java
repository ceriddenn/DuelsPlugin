package me.ceridev.duels.instance.game;

import me.ceridev.duels.instance.BasicArena;
import me.ceridev.duels.instance.DuelPlugin;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCleanup extends BukkitRunnable {

    private DuelPlugin plugin;
    private BasicArena basicArena;
    private int cleanupSeconds;

    public ArenaCleanup(DuelPlugin plugin, BasicArena basicArena) {
        this.plugin = plugin;
        this.basicArena = basicArena;
        this.cleanupSeconds = 20;
    }

    public void start() {
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        if (cleanupSeconds == 0) {
            basicArena.reset(true);
        }
        if (cleanupSeconds == 20) {
            basicArena.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn in 20 seconds...");
        } else if (cleanupSeconds == 10) {
            basicArena.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn in 10 seconds...");
        } else if (cleanupSeconds <= 3) {
            basicArena.sendMessage(ChatColor.YELLOW + "Teleporting in " + cleanupSeconds);
        }

        cleanupSeconds--;
    }
}
