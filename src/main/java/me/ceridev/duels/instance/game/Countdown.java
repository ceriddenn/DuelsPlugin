package me.ceridev.duels.instance.game;

import me.ceridev.duels.instance.BasicArena;
import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.GameState;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {

    private final DuelPlugin plugin;
    private final BasicArena basicArena;
    private int countdownSeconds;

    public Countdown(DuelPlugin plugin, BasicArena basicArena) {
        this.plugin = plugin;
        this.basicArena = basicArena;
        this.countdownSeconds = 10;
    }

    public void start() {
        basicArena.setGameState(GameState.COUNTDOWN);
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            basicArena.start();
            return;
        }

        if (countdownSeconds == 10) {
            basicArena.sendMessage(ChatColor.GREEN + "Match starting in 10 seconds!");
        } else if (countdownSeconds <= 5) {
            basicArena.sendTitle(ChatColor.RED + String.valueOf(countdownSeconds));
        }

        countdownSeconds--;
    }
}
