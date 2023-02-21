package me.ceridev.duels.instance.bridges.game;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.bridges.BridgeArena;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class BridgeCleanup extends BukkitRunnable {

    private DuelPlugin plugin;
    private BridgeArena bridgeArena;
    private int cleanupSeconds;

    public BridgeCleanup(DuelPlugin plugin, BridgeArena bridgeArena) {
        this.plugin = plugin;
        this.bridgeArena = bridgeArena;
        this.cleanupSeconds = 20;
    }

    public void start() {
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        if (cleanupSeconds == 0) {
            bridgeArena.reset(true);
        }
        if (cleanupSeconds == 20) {
            bridgeArena.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn in 20 seconds...");
        } else if (cleanupSeconds == 10) {
            bridgeArena.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn in 10 seconds...");
        } else if (cleanupSeconds <= 3) {
            bridgeArena.sendMessage(ChatColor.YELLOW + "Teleporting in " + cleanupSeconds);
        }

        cleanupSeconds--;
    }

}
