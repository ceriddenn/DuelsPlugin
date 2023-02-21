package me.ceridev.duels.instance.bridges.game;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.GameState;
import me.ceridev.duels.instance.bridges.BridgeArena;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class BridgeCountdown extends BukkitRunnable {

    private final DuelPlugin plugin;
    private final BridgeArena bridgeArena;
    private int countdownSeconds;

    public BridgeCountdown(DuelPlugin plugin, BridgeArena bridgeArena) {
        this.plugin = plugin;
        this.bridgeArena = bridgeArena;
        this.countdownSeconds = 10;
    }

    public void start() {
        bridgeArena.setGameState(GameState.COUNTDOWN);
        runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        if (countdownSeconds == 0) {
            cancel();
            bridgeArena.start();
            return;
        }

        if (countdownSeconds == 10) {
            bridgeArena.sendMessage(ChatColor.GREEN + "Match starting in 10 seconds!");
        } else if (countdownSeconds <= 5) {
            bridgeArena.sendTitle(ChatColor.RED + String.valueOf(countdownSeconds));
        }

        countdownSeconds--;
    }

}
