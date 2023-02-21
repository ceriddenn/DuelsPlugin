package me.ceridev.duels.manager.game;

import me.ceridev.duels.instance.BasicArena;
import me.ceridev.duels.instance.DuelsPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class VoidManager extends BukkitRunnable {

    private final BasicArena basicArena;

    public VoidManager(BasicArena basicArena) {
        this.basicArena = basicArena;
    }

    @Override
    public void run() {
        for (DuelsPlayer duelsPlayer : basicArena.getPlayers()) {
            if (duelsPlayer.getPlayer().getLocation().getY() <= 40) {
                // call method to teleport player back to location
            }
        }
    }
}
