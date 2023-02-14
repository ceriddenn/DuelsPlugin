package me.ceridev.duels.instance;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private final DuelPlugin plugin;
    private final Arena arena;
    private int countdownSeconds;

    public Countdown(DuelPlugin plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.countdownSeconds = 10;
    }

    public void start() {
        arena.setGameState(GameState.COUNTDOWN);
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            arena.start();
            return;
        }

        if (countdownSeconds == 10) {
            arena.sendMessage(ChatColor.YELLOW + "Game starting in 10 seconds!");
        } else if (countdownSeconds <= 5) {
            arena.sendTitle(ChatColor.RED + String.valueOf(countdownSeconds));
        }

        countdownSeconds--;
    }
}
