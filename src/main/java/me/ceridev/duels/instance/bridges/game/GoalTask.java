package me.ceridev.duels.instance.bridges.game;

import me.ceridev.duels.instance.DuelPlugin;
import me.ceridev.duels.instance.DuelsPlayer;
import me.ceridev.duels.instance.GameState;
import me.ceridev.duels.instance.bridges.BridgeArena;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class GoalTask extends BukkitRunnable {

    private BridgeArena bridgeArena;
    private DuelPlugin plugin;

    public GoalTask(DuelPlugin plugin, BridgeArena bridgeArena) {
        this.bridgeArena = bridgeArena;
        this.plugin = plugin;
        runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void run() {
        if (bridgeArena.getGameState() == GameState.RUNNING) {
            DuelsPlayer p1 = bridgeArena.getPlayers().get(0);
            DuelsPlayer p2 = bridgeArena.getPlayers().get(1);
            Location p1Loc = p1.getPlayer().getLocation();
            Location p2Loc = p2.getPlayer().getLocation();
            Location goal1 = bridgeArena.getGoal1();
            Location goal2 = bridgeArena.getGoal2();
            // exclude y axis checks
            p1Loc.setY(0);
            p2Loc.setY(0);
            goal1.setY(0);
            goal2.setY(0);
            if (p1Loc.distance(goal2) <= 1) {
                bridgeArena.addScore(p1);
            }
            if (p2Loc.distance(goal1) <= 1) {
                bridgeArena.addScore(p2);
            }
        }
    }
}
