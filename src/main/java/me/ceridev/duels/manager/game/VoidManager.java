package me.ceridev.duels.manager.game;

import me.ceridev.duels.instance.Arena;
import me.ceridev.duels.instance.DuelsPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidManager extends BukkitRunnable {

    private final Arena arena;

    public VoidManager(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void run() {
        for (DuelsPlayer duelsPlayer : arena.getPlayers()) {
            if (duelsPlayer.getPlayer().getLocation().getY() <= 40) {
                // call method to teleport player back to location
            }
        }
    }
}
