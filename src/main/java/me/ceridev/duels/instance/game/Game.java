package me.ceridev.duels.instance.game;

import me.ceridev.duels.instance.*;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Game implements Listener {

    protected DuelPlugin plugin;
    protected Arena arena;

    protected ArenaCleanup arenaCleanup;

    public Game(DuelPlugin plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;
        this.arenaCleanup = new ArenaCleanup(plugin, arena);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void start() {
        arena.setGameState(GameState.RUNNING);
        onStart();
    }

    public void startArenaCleanup() {
        arenaCleanup.start();
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
        arenaCleanup.cancel();
        this.arenaCleanup = new ArenaCleanup(plugin, arena);
    }

    public abstract void onStart();

}
