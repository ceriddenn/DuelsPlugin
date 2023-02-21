package me.ceridev.duels.instance.bridges;

import me.ceridev.duels.instance.*;
import me.ceridev.duels.instance.bridges.game.BridgeCleanup;
import me.ceridev.duels.instance.bridges.game.GoalTask;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class SpecialGame implements Listener {

    protected DuelPlugin plugin;
    protected BridgeArena bridgeArena;
    private GoalTask goalTask;
    protected BridgeCleanup bridgeCleanup;

    public SpecialGame(DuelPlugin plugin, BridgeArena bridgeArena) {
        this.plugin = plugin;
        this.bridgeArena = bridgeArena;
        this.goalTask = new GoalTask(plugin, bridgeArena);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    public void start() {
        bridgeArena.setGameState(GameState.RUNNING);
        goalTask.run();
        onStart();
    }
    public void startArenaCleanup() {
        this.bridgeCleanup = new BridgeCleanup(plugin, bridgeArena);
        bridgeCleanup.start();
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
        if (bridgeArena.getGameState() == GameState.CLOSING) {
            bridgeCleanup.cancel();
            goalTask.cancel();
        }
        this.goalTask = new GoalTask(plugin, bridgeArena);
        this.bridgeCleanup = new BridgeCleanup(plugin, bridgeArena);
    }

    public abstract void onStart();

}
