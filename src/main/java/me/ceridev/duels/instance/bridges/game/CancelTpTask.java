package me.ceridev.duels.instance.bridges.game;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.instance.GameState;
import me.ceridev.duels.instance.bridges.BridgeArena;
import org.bukkit.scheduler.BukkitRunnable;

public class CancelTpTask extends BukkitRunnable {

    private BridgeArena bridgeArena;


    public CancelTpTask(DuelPlugin plugin, BridgeArena bridgeArena) {
        this.bridgeArena = bridgeArena;
        runTaskTimer(plugin, 0 , 5);
    }

    @Override
    public void run() {
        if (bridgeArena.getGameState() == GameState.RUNNING) {
            DuelsPlayer p1 = bridgeArena.getPlayers().get(0);
            DuelsPlayer p2 = bridgeArena.getPlayers().get(1);
            p1.getPlayer().teleport(bridgeArena.getSpawn1());
            p2.getPlayer().teleport(bridgeArena.getSpawn2());
        }
    }
}
